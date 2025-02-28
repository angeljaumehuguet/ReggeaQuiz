package com.example.reggeaquiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class InfoFragment extends Fragment {

    private RecyclerView recyclerViewArtists;
    private ImageView headerImage;
    private TextView titleText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        
        // Initialize views
        headerImage = view.findViewById(R.id.header_image);
        titleText = view.findViewById(R.id.title_text);
        recyclerViewArtists = view.findViewById(R.id.recycler_view_artists);
        
        // Setup animations
        titleText.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.pulse));
        
        // Setup RecyclerView for Artists carousel
        setupArtistsRecyclerView();
        
        // Setup interactive elements
        View vinylDisc = view.findViewById(R.id.vinyl_disc);
        vinylDisc.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.rotate));
        });
        
        return view;
    }
    
    private void setupArtistsRecyclerView() {
        // This would normally use an adapter with real data
        // For this example, we'll just set the layout manager
        recyclerViewArtists.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        
        // In a real implementation, you would set an adapter with artist data
        // recyclerViewArtists.setAdapter(new ArtistsAdapter(getArtistsList()));
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Implement parallax effect on header image during scroll
        View scrollView = view.findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangeListener((View.OnScrollChangeListener) 
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            // Move the header image at a different rate to create parallax
            headerImage.setTranslationY(-scrollY / 2f);
        });
    }
}
