package ar.edu.itba.paw.models.MoovieList;

import org.hibernate.annotations.Formula;

public class MoovieListCardUserStatus {
        private int currentUserWatchAmount;
        private boolean currentUserHasLiked;
        private boolean currentUserHasFollowed;

        public MoovieListCardUserStatus(){

        }

        public MoovieListCardUserStatus(int currentUserWatchAmount, boolean currentUserHasLiked, boolean currentUserHasFollowed) {
                this.currentUserWatchAmount = currentUserWatchAmount;
                this.currentUserHasLiked = currentUserHasLiked;
                this.currentUserHasFollowed = currentUserHasFollowed;
        }

        public int getCurrentUserWatchAmount() {
                return currentUserWatchAmount;
        }

        public boolean isCurrentUserHasLiked() {
                return currentUserHasLiked;
        }

        public boolean isCurrentUserHasFollowed() {
                return currentUserHasFollowed;
        }
}