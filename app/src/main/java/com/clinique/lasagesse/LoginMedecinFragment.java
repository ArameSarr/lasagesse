package com.clinique.lasagesse;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
public class LoginMedecinFragment extends Fragment {
    private EditText editEmail, editPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    public LoginMedecinFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_medecin, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnLogin = view.findViewById(R.id.btn_login);
        // Pré-remplir avec données test
        editEmail.setText("dr.diallo@clinique.com");
        editPassword.setText("medecin123");
        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }
            if (dbHelper.verifierLoginMedecin(email, password)) {
                MedecinDashboardFragment fragment = new MedecinDashboardFragment();
                ((MainActivity) requireActivity()).loadFragment(fragment, true);
            } else {
                Toast.makeText(requireContext(), "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
