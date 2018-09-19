package es.uah.cc.todomanager;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.uah.cc.todomanager.R;

/**
 * A contact form.
 */
public class ContactActivityFragment extends Fragment {

    private Pattern pattern;

    public ContactActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        pattern = Pattern.compile("^.+@.+\\..+$");
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_contact, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send_button) {
            List<String> errorMessages = checkFields();
            if (errorMessages.isEmpty()) {
                DialogFragment dialog = new SendMessageDialog((SendMessageDialog.OnSendMessageListener) getActivity());
                dialog.show(getFragmentManager(), "send_confirmation");
            }
            else {
ErrorContactMessageDialog dialog = new ErrorContactMessageDialog(errorMessages);
dialog.show(getFragmentManager(), "ErrorContactMessage");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected List<String> checkFields() { View v = getView();
        List<String> messages = new ArrayList<String>();
        if (((EditText) v.findViewById(R.id.full_name)).getText().toString().equals(""))
            messages.add(getString(R.string.empty_name_msg));
                if (!pattern.matcher(((EditText) v.findViewById(R.id.email)).getText().toString()).matches())
                    messages.add(getString(R.string.invalid_email_msg));
        if (((EditText) v.findViewById(R.id.message_body)).getText().toString().equals(""))
            messages.add(getString(R.string.empty_body_msg));
return messages;
    }
}

