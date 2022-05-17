package com.fpp.sqlitecontacts;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contacts> {
    private Activity context;
    private List<Contacts> contacts;
    public ContactsAdapter(Activity context, List<Contacts> contacts) {
        super(context, R.layout.contacts_list_item, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    static class ViewHolder {
        public TextView tvContactsDescription;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.contacts_list_item, null, true);
            viewHolder = new ViewHolder();
            viewHolder.tvContactsDescription = rowView.findViewById(R.id.tvContactsDescription);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        Contacts task = contacts.get(position);
        viewHolder.tvContactsDescription.setText(task.getName() + " " + task.getSurname() + "\n" + task.getPhone() + "  " + task.getMail());
        /*if(task.isCompleted()) {
            viewHolder.tvContactsDescription
                    .setPaintFlags(viewHolder.tvContactsDescription.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.tvContactsDescription
                    .setPaintFlags(viewHolder.tvContactsDescription.getPaintFlags() &
                            ~Paint.STRIKE_THRU_TEXT_FLAG);
        }*/
        return rowView;
    }
}
