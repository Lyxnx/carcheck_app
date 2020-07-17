package net.lyxnx.carcheck.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.adapter.HistoryBottomSheetAdapter;
import net.lyxnx.carcheck.util.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class HistoryBottomSheetDialog extends BottomSheetDialogFragment {

    private TextView clearButton;

    private PublishSubject<History.Item> selectedListener = PublishSubject.create();

    public HistoryBottomSheetDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.history_sheet, container, false);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().setOnShowListener(dialog -> {
            View bottomSheet = ((BottomSheetDialog) dialog).findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                clearButton.setOnClickListener(i -> new AlertDialog.Builder(getContext())
                        .setTitle(getString(R.string.confirm_clear_history_title))
                        .setMessage(getString(R.string.confirm_clear_history_text))
                        .setPositiveButton(android.R.string.ok, (dialogInterface, pos) -> {
                            History.getHistory().clear();

                            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HIDDEN);
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show()
                );
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView historyRecyclerView = getView().findViewById(R.id.historyRecyclerView);

        // Wrap in arraylist copy - don't want to reverse the original
        List<History.Item> items = new ArrayList<>(History.getHistory().getItems());
        Collections.reverse(items);

        HistoryBottomSheetAdapter adapter = new HistoryBottomSheetAdapter();
        adapter.setData(items);

        clearButton = getView().findViewById(R.id.clearButton);

        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        historyRecyclerView.setAdapter(adapter);

        adapter.getClickListener()
                .subscribe(item -> {
                    selectedListener.onNext(item);
                    Dialog dialog = getDialog();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
    }

    public PublishSubject<History.Item> getSelectedListener() {
        return selectedListener;
    }
}