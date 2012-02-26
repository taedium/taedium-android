package me.taedium.android.domain;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class RecommendationOverviewList extends ArrayList<RecommendationBase> implements Parcelable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8047408653961098418L;

	public RecommendationOverviewList() {}
	
	public RecommendationOverviewList(ArrayList<RecommendationBase> recs) {
		super(recs);
	}
	
	public RecommendationOverviewList (Parcel in) {
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<RecommendationOverviewList> CREATOR = new Parcelable.Creator<RecommendationOverviewList>() {
		
		public RecommendationOverviewList createFromParcel(Parcel in) {
			return new RecommendationOverviewList(in);
		}
		
		public RecommendationOverviewList[] newArray(int arg0) {
			return null;
		}
	};
	
	private void readFromParcel(Parcel in) {
		this.clear();
        int size = in.readInt();

        for (int i = 0; i < size; i++) {
                RecommendationBase c = new RecommendationBase();
                c.id = in.readInt();
                c.name = in.readString();
                this.add(c);
        }
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.size());
		
		for (int i = 0; i < this.size(); i++) {
            RecommendationBase c = this.get(i);
            dest.writeInt(c.id);
            dest.writeString(c.name);
	    }
	}
	
	public RecommendationBase[] toRecommendationBaseArray() {
		RecommendationBase[] ret = new RecommendationBase[this.size()];
		for (int i = 0; i < this.size(); i++) {
			ret[i] = this.get(i);
		}
		return ret;
	}
}
