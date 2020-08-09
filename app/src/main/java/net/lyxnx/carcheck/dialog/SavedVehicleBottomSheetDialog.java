package net.lyxnx.carcheck.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.lyxnx.carcheck.R;
import net.lyxnx.carcheck.adapter.SavedVehicleBottomSheetAdapter;
import net.lyxnx.carcheck.managers.SavedVehicleManager;
import net.lyxnx.carcheck.model.SavedVehicle;
import net.lyxnx.carcheck.util.Singletons;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SavedVehicleBottomSheetDialog extends BottomSheetDialogFragment {

    private Context context;
    private final SavedVehicleManager savedVehicleManager;

    private PublishSubject<SavedVehicle> selectedListener = PublishSubject.create();

    public SavedVehicleBottomSheetDialog(Context context) {
        this.context = context;
        this.savedVehicleManager = Singletons.getSavedVehicleManager(context);
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

        View view = inflater.inflate(R.layout.sheet_saved, container, false);

        getDialog().setOnShowListener(dialog -> {
            View bottomSheet = ((BottomSheetDialog) dialog).findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_HALF_EXPANDED);


            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView savedRecyclerView = getView().findViewById(R.id.savedRecyclerView);

        savedVehicleManager.getSavedVehicles()
                .observe(getActivity(), response -> {
                    SavedVehicleBottomSheetAdapter adapter = new SavedVehicleBottomSheetAdapter(context);
                    adapter.setData(savedVehicleManager.getSavedVehicles().getValue());

                    savedRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    savedRecyclerView.setAdapter(adapter);

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
        return savedVehicleManager.isEmpty();
    }

    public PublishSubject<SavedVehicle> getSelectedListener() {
        return selectedListener;
    }
}