package com.nekoid.smektuber.screen.notification;

import static com.nekoid.smektuber.helpers.utils.Utils.replaceFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.nekoid.smektuber.R;
import com.nekoid.smektuber.screen.home.HomeMember;
import com.nekoid.smektuber.screen.home.account.Account;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Notif_Succes_change_Data_Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Notif_Succes_change_Data_Account extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ScrollView cdScroll;
    FrameLayout cdFragment;
    private Button BtnChangeData;

    public Notif_Succes_change_Data_Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Notif_Succes_change_Data_Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Notif_Succes_change_Data_Account newInstance(String param1, String param2) {
        Notif_Succes_change_Data_Account fragment = new Notif_Succes_change_Data_Account();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notif__succes_change__data__account, container, false);
        BtnChangeData = view.findViewById(R.id.BtnChangeDataAccount);
        cdFragment = view.findViewById(R.id.changeDataFragment);
        cdScroll = view.findViewById(R.id.changeDataScroll);
        BtnChangeData.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HomeMember.class);
            startActivity(intent);
        });
        return view;
    }
}