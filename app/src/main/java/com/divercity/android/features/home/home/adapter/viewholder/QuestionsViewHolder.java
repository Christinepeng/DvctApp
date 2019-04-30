package com.divercity.android.features.home.home.adapter.viewholder;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.divercity.android.R;
import com.divercity.android.core.utils.GlideApp;
import com.divercity.android.core.utils.Util;
import com.divercity.android.model.Question;
import com.divercity.android.repository.session.SessionRepository;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionsViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImgAuthor;
    private ImageView mImgAnswerAuthor;
    private TextView mTxtGroupName;
    private TextView mTxtQuestion;
    private TextView mTxtAnswer;
    private ImageView mImgGroup;
    private FrameLayout mLayUnseen;
    private TextView mTxtUnseen;
    private TextView mTxtAuthorTime;
    private CardView cardViewImgMainContainer;
    private ImageView imgMainPicture;

    private SessionRepository sessionRepository;
    private Listener listener;

    private QuestionsViewHolder(View itemView,
                                SessionRepository sessionRepository,
                                Listener listener) {
        super(itemView);
        this.sessionRepository = sessionRepository;
        this.listener = listener;
        mTxtGroupName = itemView.findViewById(R.id.item_quest_txt_groupname);
        mImgAnswerAuthor = itemView.findViewById(R.id.item_quest_img_answer);
        mImgAuthor = itemView.findViewById(R.id.item_group_img);
        mTxtQuestion = itemView.findViewById(R.id.item_quest_txt_author_name);
        mTxtAnswer = itemView.findViewById(R.id.item_quest_txt_answer);
        mLayUnseen = itemView.findViewById(R.id.item_quest_lay_unseen);
        mTxtUnseen = itemView.findViewById(R.id.item_quest_txt_unseen);
        mTxtAuthorTime = itemView.findViewById(R.id.item_quest_txt_author_time);
        cardViewImgMainContainer = itemView.findViewById(R.id.item_quest_cardview_pic_main);
        imgMainPicture = itemView.findViewById(R.id.item_quest_img_main);
    }

    public void bindTo(Question data) {
        String urlMain = data.getQuestionPicUrl();
        if (urlMain != null) {
            GlideApp.with(itemView)
                    .load(urlMain)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            cardViewImgMainContainer.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            cardViewImgMainContainer.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(imgMainPicture);
        } else {
            cardViewImgMainContainer.setVisibility(View.GONE);
        }

        String urlImgAuthor = data.getAuthorProfilePicUrl();
        if (urlImgAuthor != null)
            GlideApp.with(itemView)
                    .load(urlImgAuthor)
                    .apply(new RequestOptions().circleCrop())
                    .into(mImgAuthor);
        try {
            String urlImgAnswerAuthor = data.getLastAnswerAuthorPicture();
            GlideApp.with(itemView)
                    .load(urlImgAnswerAuthor).into(mImgAnswerAuthor);
        } catch (NullPointerException e) {
            GlideApp.with(itemView)
                    .load(sessionRepository.getUserAvatarUrl())
                    .into(mImgAnswerAuthor);
//            mImgAnswerAuthor.setImageResource(R.drawable.tab_profile_inactive);
        }
        mTxtQuestion.setText(data.getQuestion());
        mTxtAuthorTime.setText(" · " + data.getAuthorName() + " · " + Util.getTimeAgoWithStringServerDate(data.getCreatedAt()));
        mTxtGroupName.setText(data.getGroupTitle());
        try {
            mTxtAnswer.setText(data.getLastAnswerAuthorName() + ": " +
                    data.getLastAnswer());
        } catch (NullPointerException e) {
            mTxtAnswer.setText("");
            mTxtAnswer.setHint("Be the first to comment");
        }

        itemView.setOnClickListener(view -> {
            if (listener != null)
                listener.onQuestionClick(data);
        });
    }

    public static QuestionsViewHolder create(ViewGroup parent, SessionRepository sessionRepository, Listener listener) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_question, parent, false);
        return new QuestionsViewHolder(view, sessionRepository, listener);
    }

    public interface Listener {

        void onQuestionClick(Question question);
    }
}
