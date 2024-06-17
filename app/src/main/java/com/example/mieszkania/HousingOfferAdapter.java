package com.example.mieszkania;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HousingOfferAdapter extends ArrayAdapter<HousingOffer> {

    public HousingOfferAdapter(Context context, List<HousingOffer> offers) {
        super(context, 0, offers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_offer_details , parent, false);
            holder = new ViewHolder();
            holder.textViewMiasto = convertView.findViewById(R.id.textview_miasto);
            holder.textViewAdres = convertView.findViewById(R.id.textview_adres);
            holder.textViewPowierzchnia = convertView.findViewById(R.id.textview_powierzchnia);
            holder.textViewLiczbaPokoi = convertView.findViewById(R.id.textview_liczba_pokoi);
            holder.textViewTypNieruchomosci = convertView.findViewById(R.id.textview_typ_nieruchomosci);
            holder.textViewTyp = convertView.findViewById(R.id.textview_typ);
            holder.textViewCena = convertView.findViewById(R.id.textview_cena);
            holder.imageViewZdjecie = convertView.findViewById(R.id.imageview_zdjecie);
            holder.textViewUsername = convertView.findViewById(R.id.textview_username2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HousingOffer offer = getItem(position);

        if (offer != null && holder != null) {
            if (holder.textViewMiasto != null) {
                holder.textViewMiasto.setText(offer.getMiasto());
            }
            if (holder.textViewAdres != null) {
                holder.textViewAdres.setText(offer.getAdres());
            }
            if (holder.textViewPowierzchnia != null) {
                holder.textViewPowierzchnia.setText(String.valueOf(offer.getPowierzchnia()) + " m²");
            }
            if (holder.textViewLiczbaPokoi != null) {
                holder.textViewLiczbaPokoi.setText(String.valueOf(offer.getLiczbaPokoi()));
            }
            if (holder.textViewTypNieruchomosci != null) {
                holder.textViewTypNieruchomosci.setText(offer.getTypNieruchomosci());
            }
            if (holder.textViewTyp != null) {
                holder.textViewTyp.setText(offer.getTyp());
            }
            if (holder.textViewCena != null) {
                holder.textViewCena.setText(String.valueOf(offer.getCena()) + " PLN");
            }
            if (holder.textViewUsername != null) {
                holder.textViewUsername.setText(offer.getIdUzytkownika());
            }

            List<Bitmap> zdjecia = offer.getZdjecia();
            if (zdjecia != null && !zdjecia.isEmpty() && holder.imageViewZdjecie != null) {
                Bitmap pierwszeZdjecie = zdjecia.get(0);
                holder.imageViewZdjecie.setImageBitmap(pierwszeZdjecie);
            } else if (holder.imageViewZdjecie != null) {
                holder.imageViewZdjecie.setImageResource(R.drawable.placeholder_image); // Placeholder image
            }
        }

        // Dodanie listenera kliknięcia, aby otworzyć szczegóły oferty
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OfferDetailsActivity.class);
                intent.putExtra("OFFER", offer);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView textViewMiasto;
        TextView textViewAdres;
        TextView textViewPowierzchnia;
        TextView textViewLiczbaPokoi;
        TextView textViewTypNieruchomosci;
        TextView textViewTyp;
        TextView textViewCena;
        ImageView imageViewZdjecie;
        TextView textViewUsername;
    }
}
