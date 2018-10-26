package com.divercity.app.features.home.home.feed.adapter.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.divercity.app.R;
import com.divercity.app.core.utils.GlideApp;
import com.divercity.app.data.entity.questions.QuestionResponse;

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

    private QuestionsViewHolder(View itemView) {
        super(itemView);
        mTxtGroupName = itemView.findViewById(R.id.item_quest_txt_groupname);
        mImgAnswerAuthor = itemView.findViewById(R.id.item_quest_img_answer);
        mImgAuthor = itemView.findViewById(R.id.item_group_img);
        mTxtQuestion = itemView.findViewById(R.id.item_quest_txt_question);
        mTxtAnswer = itemView.findViewById(R.id.item_quest_txt_answer);
        mLayUnseen = itemView.findViewById(R.id.item_quest_lay_unseen);
        mTxtUnseen = itemView.findViewById(R.id.item_quest_txt_unseen);
        mTxtAuthorTime = itemView.findViewById(R.id.item_quest_txt_author_time);
        cardViewImgMainContainer = itemView.findViewById(R.id.item_quest_cardview_pic_main);
        imgMainPicture = itemView.findViewById(R.id.item_quest_img_main);
    }

    public void bindTo(QuestionResponse data) {
        try{
            cardViewImgMainContainer.setVisibility(View.VISIBLE);
            String urlMain = data.getAttributes().getPictureMain();
            GlideApp.with(itemView.getContext())
                    .load(urlMain).into(imgMainPicture);
        } catch (NullPointerException e){
            cardViewImgMainContainer.setVisibility(View.GONE);
        }


        String urlImgAuthor = data.getAttributes().getAuthorInfo().getAvatarMedium();
        if (urlImgAuthor != null)
            GlideApp.with(itemView.getContext())
                    .load(data.getAttributes().getAuthorInfo().getAvatarMedium()).into(mImgAuthor);
        try {
            String urlImgAnswerAuthor = data.getAttributes().getLastActivityInfo().getAuthorInfo().getAvatarThumb();
            GlideApp.with(itemView.getContext())
                    .load(urlImgAnswerAuthor).into(mImgAnswerAuthor);
        } catch (NullPointerException e) {
            mImgAnswerAuthor.setImageResource(R.drawable.tab_profile_inactive);
        }
        mTxtQuestion.setText(data.getAttributes().getText());
        mTxtAuthorTime.setText(" · " + data.getAttributes().getAuthorInfo().getNickname() + " · ");
        mTxtGroupName.setText(data.getAttributes().getGroup().get(0).getTitle());
        try {
            mTxtAnswer.setText(data.getAttributes().getLastActivityInfo().getAuthorInfo().getName() + ": " +
                    data.getAttributes().getLastActivityInfo().getAnswerInfo().getText());
        } catch (NullPointerException e) {
            mTxtAnswer.setText("No activity");
        }
    }

    public static QuestionsViewHolder create(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_question, parent, false);
        return new QuestionsViewHolder(view);
    }

}
