package com.clinique.lasagesse;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
public class LoginSecretaireFragment extends Fragment {
    private EditText editIdentifiant, editPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    public LoginSecretaireFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_secretaire, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editIdentifiant = view.findViewById(R.id.edit_identifiant);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);
        // Pré-remplir données test
        editIdentifiant.setText("marie.diallo@clinique.com");
        editPassword.setText("secretaire123");
        btnLogin.setOnClickListener(v -> {
            String identifiant = editIdentifiant.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if (identifiant.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dbHelper.verifierLoginSecretaire(identifiant, password)) {
                SecretaireDashboardFragment fragment = new SecretaireDashboardFragment();
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(), "Identifiant ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}