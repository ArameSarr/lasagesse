package com.clinique.lasagesse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RendezVousAdapter extends RecyclerView.Adapter<RendezVousAdapter.ViewHolder> {

    private List<RendezVous> rdvList;
    private OnRdvClickListener clickListener;

    // Interface pour gérer les clics
    public interface OnRdvClickListener {
        void onRdvClick(RendezVous rdv);
    }

    public RendezVousAdapter(List<RendezVous> rdvList) {
        this.rdvList = rdvList;
    }

    public RendezVousAdapter(List<RendezVous> rdvList, OnRdvClickListener listener) {
        this.rdvList = rdvList;
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rendezvous, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RendezVous rdv = rdvList.get(position);
        holder.bind(rdv);

        // Ajouter le click listener si défini
        if (clickListener != null) {
            holder.itemView.setOnClickListener(v -> clickListener.onRdvClick(rdv));
        }
    }

    @Override
    public int getItemCount() {
        return rdvList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvHeure, tvMedecin, tvMotif, tvStatut;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvHeure = itemView.findViewById(R.id.tv_heure);
            tvMedecin = itemView.findViewById(R.id.tv_medecin);
            tvMotif = itemView.findViewById(R.id.tv_motif);
            tvStatut = itemView.findViewById(R.id.tv_statut);
        }

        void bind(RendezVous rdv) {
            tvDate.setText(rdv.getDate());
            tvHeure.setText(rdv.getHeure());
            tvMedecin.setText(rdv.getMedecinNom() + " - " + rdv.getSpecialite());
            tvMotif.setText(rdv.getMotif());
            tvStatut.setText(rdv.getStatut());

            // Changer la couleur selon le statut
            switch (rdv.getStatut().toLowerCase()) {
                case "confirmé":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_confirme);
                    break;
                case "planifié":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_planifie);
                    break;
                case "annulé":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_annule);
                    break;
                case "en cours":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_en_cours);
                    break;
                case "à confirmer":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_planifie);
                    break;
                case "urgent":
                    tvStatut.setBackgroundResource(R.drawable.bg_status_annule);
                    break;
            }
        }
    }
}