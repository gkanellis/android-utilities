package gr.gkanellis.utilities.recyclerview;

public interface RecyclerViewModel {

    int NO_VIEW_TYPE = 0;

    default int getViewType() {
        return NO_VIEW_TYPE;
    }

}
