package com.example.cinefast;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;

public class FragmentMyBookings extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookings = new ArrayList<>();

    private ListenerRegistration bookingsListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_bookings, container, false);
        recyclerView = v.findViewById(R.id.rv_my_bookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new BookingAdapter(requireContext(), bookings, booking -> showCancelDialog(booking));
        recyclerView.setAdapter(adapter);

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid != null) {
            bookingsListener = FirebaseFirestore.getInstance()
                    .collection("bookings").document(uid)
                    .collection("bookings")
                    .addSnapshotListener((snapshot, error) -> {
                        if (error != null || snapshot == null) {
                            Toast.makeText(requireContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        bookings.clear();
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Booking b = doc.toObject(Booking.class);
                            if (b != null) {
                                b.bookingId = doc.getId();
                                bookings.add(b);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    });
        }

        return v;
    }

    private void showCancelDialog(Booking booking) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel this booking?")
                .setPositiveButton("Yes", (dialog, which) -> attemptCancel(booking))
                .setNegativeButton("No", null)
                .show();
    }

    private void attemptCancel(Booking booking) {
        long now = System.currentTimeMillis();
        if (booking.showTimestamp <= now) {
            Toast.makeText(requireContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (uid != null && booking.bookingId != null) {
            FirebaseFirestore.getInstance()
                    .collection("bookings").document(uid)
                    .collection("bookings").document(booking.bookingId)
                    .delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to cancel booking", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bookingsListener != null) {
            bookingsListener.remove();
        }
    }
}