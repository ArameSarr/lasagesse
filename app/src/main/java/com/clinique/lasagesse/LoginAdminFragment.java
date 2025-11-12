package com.clinique.lasagesse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class LoginAdminFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;

    public LoginAdminFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_admin, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);

        // Pré-remplir avec données test
        editEmail.setText("admin@clinique.com");
        editPassword.setText("admin123");

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.verifierLoginAdmin(email, password)) {
                // Récupérer les informations de l'admin
                Admin admin = dbHelper.getAdminByEmail(email);

                AdminDashboardFragment fragment = AdminDashboardFragment.newInstance(
                        admin.getId(),
                        admin.getPrenom(),
                        admin.getRole()
                );
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}