package com.clinique.lasagesse;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
public class RegisterSecretaireFragment extends Fragment {
    private EditText editNom, editPrenom, editEmail, editPassword;
    private Button btnRegister;
    private DatabaseHelper dbHelper;
    public RegisterSecretaireFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_secretaire, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        editNom = view.findViewById(R.id.edit_nom);
        editPrenom = view.findViewById(R.id.edit_prenom);
        editEmail = view.findViewById(R.id.edit_email);
        editPassword = view.findViewById(R.id.edit_password);
        btnRegister = view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(v -> {
            if (validerFormulaire()) {
                boolean success = dbHelper.ajouterSecretaire(
                        editNom.getText().toString().trim(),
                        editPrenom.getText().toString().trim(),
                        editEmail.getText().toString().trim(),
                        editPassword.getText().toString().trim()
                );
                if (success) {
                    Toast.makeText(requireContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(requireContext(), "Erreur lors de l'inscription, email déjà utilisé.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private boolean validerFormulaire() {

            if (TextUtils.isEmpty(editNom.getText())) {
                editNom.setError("Nom requis");
                return false;
            }
            if (TextUtils.isEmpty(editPrenom.getText())) {
                editPrenom.setError("Prénom requis");
                return false;
            }
            if (TextUtils.isEmpty(editEmail.getText())) {
                editEmail.setError("Email requis");
                return false;
            }
            if (TextUtils.isEmpty(editPassword.getText())) {
                editPassword.setError("Mot de passe requis");
                return false;
            }
            return true;
        }
    }
