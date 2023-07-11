package com.example.myprojectprm;

import static android.content.Context.MODE_PRIVATE;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.widget.SearchView;

import com.google.android.material.tabs.TabLayout;
import com.example.myprojectprm.CircleIndicatorView;


public class ProductFragment extends Fragment {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseHelper databaseHelper;
    private static final String TABLE_PRODUCTS = "products";

    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "product_description";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";

    private static final String CHANNEL_ID = "cart_notification_channel";
    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    private static List<Cart> cartList;

    private String loggedInUsername; // Variable to store the username of the logged-in user

    //banner
    //change banner auto
    private static final long BANNER_DELAY_MS = 3000; // Delay between banner image changes in milliseconds
    private Handler bannerHandler;
    private Runnable bannerRunnable;
    private ViewPager2 carouselViewPager; // Declare carouselViewPager variable here
    private CircleIndicatorView indicatorView;
    List<CarouselItem> carouselItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

// Retrieve the passed username from the arguments
        loggedInUsername = getArguments().getString("username");
//        Log.d("product", loggedInUsername);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity(), productList);
        recyclerView.setAdapter(productAdapter);

        databaseHelper = new DatabaseHelper(requireContext());

        populateProductList();

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Product clickedProduct = productList.get(position);

                // Create a new instance of the ProductDetailFragment
                ProductDetailFragment productDetailFragment = new ProductDetailFragment();

                // Pass the clicked product details to the fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", clickedProduct);
                productDetailFragment.setArguments(bundle);

                // Replace the current fragment with the ProductDetailFragment
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, productDetailFragment)
                        .addToBackStack(null) // Add the fragment to the back stack to enable back navigation
                        .commit();
            }
        });

        //search
        SearchManager searchManager = (SearchManager) requireActivity().getSystemService(Context.SEARCH_SERVICE);

        searchView = view.findViewById(R.id.edtSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        //function search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.getFilter().filter(newText);
                return false;
            }
        });

        //sort by price
        ImageView sortImageView = view.findViewById(R.id.iv_sort);

        final boolean[] ascendingOrder = {true};

        sortImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ascendingOrder[0] = !ascendingOrder[0];

                Collections.sort(productList, new Comparator<Product>() {
                    @Override
                    public int compare(Product p1, Product p2) {
                        double price1 = Double.parseDouble(p1.getPrice());
                        double price2 = Double.parseDouble(p2.getPrice());

                        int result;
                        if (ascendingOrder[0]) {
                            result = Double.compare(price1, price2);
                        } else {
                            result = Double.compare(price2, price1);
                        }

                        return result;
                    }
                });

                productAdapter.notifyDataSetChanged();
            }
        });

        //banner
         carouselViewPager = view.findViewById(R.id.carouselViewPager);

// Create a list of carousel items
        carouselItems = new ArrayList<>();
        carouselItems.add(new CarouselItem(R.drawable.banner1));
        carouselItems.add(new CarouselItem(R.drawable.banner2));
        carouselItems.add(new CarouselItem(R.drawable.banner3));
        carouselItems.add(new CarouselItem(R.drawable.banner4));
        carouselItems.add(new CarouselItem(R.drawable.banner5));

// Create an adapter for the carousel items
        CarouselAdapter carouselAdapter = new CarouselAdapter(carouselItems);
        carouselViewPager.setAdapter(carouselAdapter);



        //indicator dot
        indicatorView = view.findViewById(R.id.indicatorView);
        indicatorView.setIndicators(carouselItems.size());

        // Update the selected indicator when the carousel page changes
        carouselViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                indicatorView.selectIndicator(position);
            }
        });

        //auto change banner
        startBannerAutoChange();



    }

    private void populateProductList() {
        productList.clear();

        productList.add(new Product(1, "MSI Thin GF63 15.6 144Hz Gaming Laptop", "12th Gen Intel Core i7 Processor: Performance to stream and game anywhere. Designed to deliver high-performance gameplay.\n" +
                "It All Starts Now: Enjoy the latest generation Windows 11 Home for your everyday needs. *MSI recommends Windows 11 Pro for business use.", "10.99", R.drawable.gigabyte));
        productList.add(new Product(2, "HP - Pavilion x360 2-in-1 14\" Touch-Screen Laptop", "Operating system: Windows 11 Home From a rejuvenated Start menu, to new ways to connect to your favorite people, news, games, and content—Windows 11 is the place to think, express, and create in a natural way.\n" +
                "Display: 14.0-inch diagonal, FHD (1920 x 1080), multitouch-enabled, IPS, edge-to-edge glass, microedge Crystal-clear visuals with 178-degree wide-viewing angles.", "19.99", R.drawable.hp));
        productList.add(new Product(3, "Lenovo Legion Pro 5 2023 16\" Gaming Laptop WQXGA", "Powerhouse 13th Intel Core i9-13900HX 24-Core (Up to 5.4 GHz with Intel Turbo Boost Technology, 36 MB Intel Smart cache, 8P+16E cores, 32 threads)\n" +
                "【Customization】Upgraded to 64GB DDR5 SDRAM 4800 MHz | 2TB PCI-E NVMe Solid State Drive | Windows 11 Pro", "15.99", R.drawable.lenovo));
        productList.add(new Product(4, "Braun Electric Series 3 Razor with Precision Trimmer", "CLOSE, CLEAN SHAVE: 3 blade free floating system follows facial contours for a flawless finish and includes a middle trimmer to cut difficult hairs along with 2x SensoFoil shaving elements for ultimate closeness and skin comfort\n" +
                "CAPTURES MORE HAIR: Precision trimmer for accurate moustache and sideburn trimming, includes a specialized MicroComb designed to capture more hair in each stroke *compared to Braun series 3, tested on 3 day beards", "49.94", R.drawable.braunrazor));
        productList.add(new Product(5, "Focal Bathys Over-Ear Hi-Fi Bluetooth Wireless Headphones", "Description 3", "699", R.drawable.dysonheadphone));
        productList.add(new Product(6, "Razer BlackShark V2 X Gaming Headset", "Active noise cancelling for full immersion and transparency mode to interact with your environment\n" +
                "Refined real leather and microfiber headband are incomparably soft comfortable and refined\n" +
                "Limitless connectivity with a 3.5mm jack USB-C outlet and Bluetooth capability\n" +
                "Aluminium-magnesium construction for pure sound reliability resistance and increased lightness", "669.99", R.drawable.razerheadphone));
        productList.add(new Product(7, "Grole BlackShark V2 X Gaming Headset - for PC", "Advanced Passive Noise Cancellation: sturdy closed earcups fully cover ears to prevent noise from leaking into the headset, with its cushions providing a closer seal for more sound isolation.\n" +
                "7.1 Surround Sound for Positional Audio: Outfitted with custom-tuned 50 mm drivers, capable of software-enabled surround sound. *Only available on Windows 10 64bit\n" +
                "Triforce Titanium 50mm High-End Sound Drivers: With titanium-coated diaphragms for added clarity, our new, cutting-edge proprietary design divides the driver into 3 parts for the individual tuning of highs, mids, and lows—producing brighter, clearer audio with richer highs and more powerful lows", "48.66", R.drawable.gillettterazor));
        productList.add(new Product(8, "Gillette Fusion ProGlide Razors, Men 1 Gillette Razor", "SHARPEST BLADES: Our sharpest blades (first 4 blades) help get virtually every hair effortlessly\n" +
                "FLEXBALL HANDLE: FlexBall technology that responds to contours\n" +
                "5-BLADES: 5 anti-friction blades for a close, long-lasting shave", "20.87", R.drawable.gillettterazor));
        productList.add(new Product(9, "iPhone 13 Pro Max, 256GB, Sierra Blue", "nlocked\n" +
                "Tested for battery health and guaranteed to come with a battery that exceeds 90% of original capacity.\n" +
                "Inspected and guaranteed to have minimal cosmetic damage, which is not noticeable when the device is held at arm’s length. Successfully passed a full diagnostic test which ensures like-new functionality and removal of any prior-user personal information.\n" +
                "Includes a brand new, generic charging cable that is certified Mfi (Made for iPhone) and a brand new, generic wall plug that is UL certified for performance and safety. Also includes a SIM tray removal tool but does not come with headphones or a SIM card.", "999.00", R.drawable.iphone14));
        productList.add(new Product(10, "Xiaomi Mi 11 Lite 5G + 4G LTE Volte Global", "International Model - No Warranty in US. Compatible with Most GSM Carriers like T-Mobile, AT&T, MetroPCS, etc. Will NOT work with CDMA Carriers Such as Verizon, Sprint, Boost. - FCC ID: 2AFZZK7BNY\n" +
                "5G NR:n1/n3/n5/n7/n8/n20/n28/n38/n40/n41/n77/n78/n66 4G: LTE FDD B1/2/3/4/5/7/8/20/28 LTE TDD B38/40/41 3G: B1/2/4/5/8 2g Quad Band\n" +
                "64Mp Camera Quad Camera 64+8+5 / 20 mp front camera", "349.99", R.drawable.mi11lite));

        for (Product product : productList) {
            saveProductToDatabase(product);
        }

        productAdapter.notifyDataSetChanged();
    }

    private void saveProductToDatabase(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_NAME, product.getName());
        values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(COLUMN_PRODUCT_PRICE, product.getPrice());

        long productId = db.insert(TABLE_PRODUCTS, null, values);

        product.setId((int) productId);

        db.close();
    }


    //auto change banenr method
    private void startBannerAutoChange() {
        bannerHandler = new Handler();
        bannerRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = carouselViewPager.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= carouselItems.size()) {
                    nextItem = 0; // Wrap around to the first item
                }
                carouselViewPager.setCurrentItem(nextItem);
                bannerHandler.postDelayed(this, BANNER_DELAY_MS);
            }
        };
        bannerHandler.postDelayed(bannerRunnable, BANNER_DELAY_MS);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerHandler != null && bannerRunnable != null) {
            bannerHandler.removeCallbacks(bannerRunnable);
            bannerHandler = null;
            bannerRunnable = null;
        }
    }

}