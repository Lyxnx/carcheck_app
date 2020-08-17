package net.lyxnx.carcheck.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.adapter.HistoryBottomSheetAdapter;
import net.lyxnx.carcheck.managers.HistoryManager;
import net.lyxnx.carcheck.model.SavedVehicle;
import net.lyxnx.carcheck.util.Singletons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class HistoryBottomSheetDialog extends BottomSheetDialogFragment {

    private final Context context;
    private final HistoryManager historyManager;
    private TextView clearButton;

    private final PublishSubject<SavedVehicle> selectedListener = PublishSubject.create();

    public HistoryBottomSheetDialog(Context context) {
        this.context = context;
        this.historyManager = Singletons.getHistoryManager(context);
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

        View view = inflater.inflate(R.layout.sheet_history, container, false);

        getDialog().setOnShowListener(dialog -> {
            View bottomSheet = ((BottomSheetDialog) dialog).findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

                clearButton.setOnClickListener(i -> new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogStyle))
                        .setTitle(getString(R.string.confirm_clear_history_title))
                        .setMessage(getString(R.string.confirm_clear_history_text))
                        .setPositiveButton(android.R.string.ok, (dialogInterface, pos) -> {
                            historyManager.clear();

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

        clearButton = getView().findViewById(R.id.clearButton);

        RecyclerView historyRecyclerView = getView().findViewById(R.id.historyRecyclerView);

        historyManager.getSavedVehicles()
                .observe(getActivity(), response -> {
                    List<SavedVehicle> items = new ArrayList<>(response);
                    Collections.reverse(items);

                    HistoryBottomSheetAdapter adapter = new HistoryBottomSheetAdapter();
                    adapter.setData(items);

                    historyRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    historyRecyclerView.setAdapter(adapter);

                    adapter.getClickListener()
                            .subscribe(item -> {
                                selectedListener.onNext(item);
                                Dialog dialog = getDialog();
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            });
                });
    }

    public boolean hasItems() {
        return historyManager.isEmpty();
    }

    public PublishSubject<SavedVehicle> getSelectedListener() {
        return selectedListener;
    }
}