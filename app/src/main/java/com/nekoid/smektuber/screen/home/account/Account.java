package com.nekoid.smektuber.screen.home.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.nekoid.smektuber.R;
import com.nekoid.smektuber.api.Endpoint;
import com.nekoid.smektuber.api.ImageUrlUtil;
import com.nekoid.smektuber.api.PublicApi;
import com.nekoid.smektuber.app.BaseFragment;
import com.nekoid.smektuber.helpers.navigation.Navigator;
import com.nekoid.smektuber.helpers.utils.State;
import com.nekoid.smektuber.helpers.utils.Utils;
import com.nekoid.smektuber.models.UserModel;
import com.nekoid.smektuber.network.Http;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
    public class Account extends BaseFragment {

        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";
        private String mParam1;
        private String mParam2;
        private TextView tvFullName, tvUsername;
        ShimmerFrameLayout shimmerFrameLayout;
        private ShapeableImageView imageView;
        RelativeLayout layoutAccount;
        private UserModel userModel;
        boolean withAnimation = true;
        public Account() {
            // Required empty public constructor
        }


        // TODO: Rename and change types and number of parameters
        public static Account newInstance(String param1, String param2) {
            Account fragment = new Account();
            Bundle args = new Bundle();
            args.putString(ARG_PARAM1, param1);
            args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                mParam1 = getArguments().getString(ARG_PARAM1);
                mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        @SuppressLint("CutPasteId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_account, container, false);
            tvFullName = view.findViewById(R.id.FullnameAccount);
            tvUsername = view.findViewById(R.id.UsernameAccount);
            shimmerFrameLayout = view.findViewById(R.id.ShimmerAccount);
            layoutAccount = view.findViewById(R.id.LayoutProfil);
            imageView = view.findViewById(R.id.ImageProfil);
            Button btnUpdate = view.findViewById(R.id.ButtonUbahProfil);
            Button btnLogout = view.findViewById(R.id.ButtonKeluarAkun);
            ImageView imageZoom = view.findViewById(R.id.ImageProfil);
            imageZoom.setOnClickListener(v -> {
                Navigator.of(getActivity()).push(ViewZoomImage.class).animation( R.anim.fade_in, R.anim.fade_out );
            });
            btnUpdate.setOnClickListener(v -> {
                Navigator.of(getActivity()).push(ChangeDataAccount.class);
            });
            btnLogout.setOnClickListener(v -> {
                Http.post(Endpoint.LOGOUT.getUrl(), PublicApi.getHeaders(), this::doLogout);
            });
            startShimmer();
            if (State.userModel != null) {
                withAnimation = false;
                userModel = State.userModel;
                setModelToView();
                init();
            } else {
                withAnimation = true;
                init();
            }

            return view;
        }

        private void startShimmer() {

        }

        public void stopShimmer() {
            shimmerFrameLayout.setVisibility(View.GONE);
            layoutAccount.setVisibility(View.VISIBLE);
            if (withAnimation) layoutAccount.setAnimation(Utils.animation());
        }

        private void init() {
            // Get user data from State
            userModel = State.userModel;

            if (userModel !=null && userModel.avatar != null && !userModel.avatar.isEmpty()) {
                if (userModel.avatar.startsWith("http://") || userModel.avatar.startsWith("https://")) {
                    Http.loadImageWithoutCache(userModel.avatar, imageView, this::setModelToView);
                }
            }
            setModelToView();
        }

        private void setModelToView() {
            if (userModel != null){
                tvFullName.setText(userModel.name);
                tvUsername.setText(userModel.username);
            }
            stopShimmer();
        }

        @Override
        public void onResume() {
            super.onResume();
            init();
        }
}