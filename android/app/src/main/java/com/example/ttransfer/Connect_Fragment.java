package com.example.ttransfer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Connect_Fragment extends Fragment {

    private EditText host;
    private EditText port;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.connect_fragment, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MainActivity.MODE_PRIVATE);
        host = rootView.findViewById(R.id.host_edit_text);
        host.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidInput(host.getText().toString())) {
                    host.setTextColor(Color.RED);
                } else {
                    host.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        port = rootView.findViewById(R.id.port_edit_text);
        port.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!isValidInput(port.getText().toString())) {
                    port.setTextColor(Color.RED);
                } else {
                    port.setTextColor(getResources().getColor(R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        Button button_confirm_connect = rootView.findViewById(R.id.button_confirm_connect);
        button_confirm_connect.setOnClickListener(this::confirmInput);
        String saved_host = sharedPreferences.getString("host","");
        host.setText(saved_host);
        String saved_port = sharedPreferences.getString("port","");
        port.setText(saved_port);
        return rootView;
    }

    public void confirmInput(View view) {
        String host = this.host.getText().toString();
        String port = this.port.getText().toString();

        if (!isValidInput(host) || !isValidInput(port)) {
            if (!isValidInput(host)) {
                this.host.setTextColor(Color.RED);
            } else if (!isValidInput(port)) {
                this.port.setTextColor(Color.RED);
            } else if (!isValidInput(host) || !isValidInput(port)) {
                this.host.setTextColor(Color.RED);
                this.port.setTextColor(Color.RED);
            }
            return;
        }

        this.host.setTextColor(getResources().getColor(R.color.black));
        this.port.setTextColor(getResources().getColor(R.color.black));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("host", host);
        editor.putString("port", port);
        editor.apply();

        // Close the fragment
        getParentFragmentManager().beginTransaction().remove(this).commit();
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.set_host(host);
            mainActivity.set_port(port);
            mainActivity.set_button_connect_visible();
            mainActivity.set_button_start_visible();
        }
    }

    private boolean isValidInput(String input) {
        // Check if input contains only digits and dots
        return input.matches("[0-9.]+");
    }
}
