package com.clinique.lasagesse;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
public class LoginPatientFragment extends Fragment {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnRegister;
    private DatabaseHelper dbHelper;

    public LoginPatientFragment() {
        // Required empty public constructor
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_patient, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);
        btnRegister = view.findViewById(R.id.btn_register);

        // Pré-remplir avec des données de test
        editEmail.setText("fatou.fall@email.com");
        editPassword.setText("patient123");
        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.verifierLoginPatient(email, password)) {
                // Charger le dashboard patient
                PatientDashboardFragment fragment = new PatientDashboardFragment();
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });
        btnRegister.setOnClickListener(v -> {
            // Charger le fragment d'inscription
            RegisterPatientFragment fragment = new RegisterPatientFragment();
            ((MainActivity) requireActivity()).loadFragment(fragment, true);
        });

        return view;
    }
}