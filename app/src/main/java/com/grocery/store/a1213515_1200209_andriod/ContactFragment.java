package com.grocery.store.a1213515_1200209_andriod;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    private static final String STORE_PHONE = "+970599000000";
    private static final String STORE_EMAIL = "SmartGroceryStore@store.com";
    private static final String BZU_LATITUDE = "32.0108";
    private static final String BZU_LONGITUDE = "35.2034";

    private Button callButton, emailButton, mapButton;

    public ContactFragment() {
        // Required empty public constructor
    }

    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initViews(view);
        setupClickListeners();
        setupAnimations(view);

        return view;
    }

    private void initViews(View view) {
        callButton = view.findViewById(R.id.call_button);
        emailButton = view.findViewById(R.id.email_button);
        mapButton = view.findViewById(R.id.map_button);
    }

    private void setupClickListeners() {
        callButton.setOnClickListener(v -> openPhoneApp());
        emailButton.setOnClickListener(v -> openEmailApp());
        mapButton.setOnClickListener(v -> openGoogleMaps());
    }

    private void setupAnimations(View view) {
        try {
            Animation slideInLeft = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_left);
            Animation slideInRight = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);

            view.findViewById(R.id.contact_content).startAnimation(fadeIn);
            callButton.startAnimation(slideInLeft);
            emailButton.startAnimation(slideInRight);
            mapButton.startAnimation(slideInLeft);
        } catch (Exception e) {
            // Animation files might not exist, continue without animations
        }
    }

    private void openPhoneApp() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + STORE_PHONE));
            startActivity(callIntent);

            // Add button animation if available
            try {
                Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                callButton.startAnimation(scaleUp);
            } catch (Exception e) {
                // Animation not available, continue
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to open phone app", Toast.LENGTH_SHORT).show();
        }
    }

    private void openEmailApp() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + STORE_EMAIL));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Inquiry - Smart Grocery Store");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello,\n\nI would like to inquire about...\n\nBest regards,");

            startActivity(emailIntent);

            // Add button animation if available
            try {
                Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                emailButton.startAnimation(scaleUp);
            } catch (Exception e) {
                // Animation not available, continue
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to open email app", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmailFallback() {
        // Create an intent to copy email to clipboard and show message
        try {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    requireActivity().getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Email", STORE_EMAIL);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(getContext(),
                    "Email address copied to clipboard: " + STORE_EMAIL,
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(),
                    "Please email us at: " + STORE_EMAIL,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void openGoogleMaps() {
        try {
            // BZU location coordinates
            String locationUri = "geo:" + BZU_LATITUDE + "," + BZU_LONGITUDE + "?q=" +
                    BZU_LATITUDE + "," + BZU_LONGITUDE + "(Birzeit University - Smart Grocery Store)";

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUri));
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(mapIntent);

                // Add button animation if available
                try {
                    Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
                    mapButton.startAnimation(scaleUp);
                } catch (Exception e) {
                    // Animation not available, continue
                }
            } else {
                // If Google Maps is not installed, open in browser
                String browserUri = "https://www.google.com/maps?q=" + BZU_LATITUDE + "," + BZU_LONGITUDE;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(browserUri));
                startActivity(browserIntent);
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to open maps", Toast.LENGTH_SHORT).show();
        }
    }
}