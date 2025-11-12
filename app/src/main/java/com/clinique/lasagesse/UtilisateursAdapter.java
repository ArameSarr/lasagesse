package com.clinique.lasagesse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UtilisateursAdapter<T> extends RecyclerView.Adapter<UtilisateursAdapter.ViewHolder> {

    public interface OnItemActionListener<T> {
        void onAction(T item);
    }

    private List<T> items;
    private OnItemActionListener<T> onEditListener;
    private OnItemActionListener<T> onDeleteListener;

    public UtilisateursAdapter(List<T> items,
                               OnItemActionListener<T> onEditListener,
                               OnItemActionListener<T> onDeleteListener) {
        this.items = items;
        this.onEditListener = onEditListener;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_utilisateur, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T item = items.get(position);

        if (item instanceof Patient) {
            Patient patient = (Patient) item;
            holder.tvNom.setText(patient.getNom() + " " + patient.getPrenom());
            holder.tvInfo1.setText("📧 " + patient.getEmail());
            holder.tvInfo2.setText("📞 " + patient.getTelephone());
            holder.tvType.setText("Patient");
            holder.tvType.setBackgroundResource(R.drawable.bg_status_confirme);

        } else if (item instanceof Medecin) {
            Medecin medecin = (Medecin) item;
            holder.tvNom.setText("Dr. " + medecin.getNom() + " " + medecin.getPrenom());
            holder.tvInfo1.setText("📧 " + medecin.getEmail());
            holder.tvInfo2.setText("🏥 " + medecin.getSpecialite() + " - " + medecin.getCabinet());
            holder.tvType.setText("Médecin");
            holder.tvType.setBackgroundResource(R.drawable.bg_status_en_cours);

        } else if (item instanceof Secretaire) {
            Secretaire secretaire = (Secretaire) item;
            holder.tvNom.setText(secretaire.getNom() + " " + secretaire.getPrenom());
            holder.tvInfo1.setText("📧 " + secretaire.getEmail());
            holder.tvInfo2.setVisibility(View.GONE);
            holder.tvType.setText("Secrétaire");
            holder.tvType.setBackgroundResource(R.drawable.bg_status_planifie);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (onEditListener != null) {
                onEditListener.onAction(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteListener != null) {
                onDeleteListener.onAction(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvInfo1, tvInfo2, tvType;
        ImageButton btnEdit, btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tv_nom);
            tvInfo1 = itemView.findViewById(R.id.tv_info1);
            tvInfo2 = itemView.findViewById(R.id.tv_info2);
            tvType = itemView.findViewById(R.id.tv_type);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}