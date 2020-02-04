//api level 22
package com.example.chitterchatter;



import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChitFragment extends Fragment {
    //defining various things
    private OkHttpClient mHTTPClient;
    private RecyclerView mRecycler;
    private RecyclerView.Adapter<ChitHolder> mAdapter = new ChitAdapter();
    private Button mSend;
    private EditText mSendView;
    private String currMessage;
    private Button mRefresh;
    private ArrayList<Chat> mChatList;
    private ArrayList<String> mLiked;
    private ArrayList<String> mDisliked;
    private String currLDL;
    private String currId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chit_layout_recycler, container, false);

        //setup send button
        mSend = (Button) v.findViewById(R.id.send_button);
        mSend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            //gets string then launches into the sending process
                                            String msg = mSendView.getText().toString();
                                            currMessage = msg;
                                            mSendView.setText("");
                                            sendMessage(msg);
                                            refresh();

                                        }
                                    }
        );

        //setup refresh button
        mRefresh = (Button) v.findViewById(R.id.refreshButton);
        mRefresh.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            refresh();
                                        }
                                    }
        );
        //setup the editable text view
        mSendView = (EditText) v.findViewById(R.id.send_view);
        //setup arraylist of chats
        mChatList = new ArrayList<Chat>();
        //setup arraylists to keep track of whats been liked and disliked
        mLiked = new ArrayList<String>();
        mDisliked = new ArrayList<String>();

        // setup recycler view
        mRecycler = (RecyclerView) v.findViewById(R.id.chit_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(mAdapter);

        mHTTPClient = new OkHttpClient();
        //load first batch
        refresh();

        return v;
    }

    // represents a message displayer thing
    private class ChitHolder extends RecyclerView.ViewHolder {
        //instantiate holder specific objects
        private TextView mMessage;
        private TextView mClient;
        private TextView mDate;
        private Button mLike;
        private Button mDislike;
        private int mPosition;
        private Chat chatHere;

        public ChitHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.message_layout, container, false));
            //link display and code
            mMessage = (TextView) itemView.findViewById(R.id.message_view);
            mClient = (TextView) itemView.findViewById(R.id.client_view);
            mDate = (TextView) itemView.findViewById(R.id.date_view);
            //setup like button
            mLike = (Button) itemView.findViewById(R.id.like_button);
            //changes the model then updates the view
            mLike.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if(!mLiked.contains(chatHere.get_id()) && !mDisliked.contains(chatHere.get_id())){
                                                 mLiked.add(chatHere.get_id());
                                                 like(chatHere.get_id());
                                             }
                                         }
                                     }
            );
            //setup dislike button
            mDislike = (Button) itemView.findViewById(R.id.dislie_button);
            //changes the model then updates the view
            mDislike.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if(!mLiked.contains(chatHere.get_id()) && !mDisliked.contains(chatHere.get_id())){
                                                 mDisliked.add(chatHere.get_id());
                                                 dislike(chatHere.get_id());
                                             }
                                         }
                                     }
            );

        }

        public void bindPosition(int p) {
            mPosition = p;
        }
    }



    private class ChitAdapter extends RecyclerView.Adapter<ChitHolder> {
        @Override
        public void onBindViewHolder(ChitHolder holder, int position) {
            // tell holder which place on list it is representing
            holder.bindPosition(position);
            // setup what the message should hold
            holder.chatHere = mChatList.get(position);
            holder.mMessage.setText(holder.chatHere.getmContent());
            holder.mClient.setText("Posted by: " + holder.chatHere.getUser());
            holder.mDate.setText("On: " + holder.chatHere.getDate());
            holder.mDislike.setText(String.valueOf(holder.chatHere.getmDislikes()));
            holder.mLike.setText(String.valueOf(holder.chatHere.getmLikes()));

        }

        @Override
        public ChitHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ChitHolder(inflater, parent);
        }

        @Override
        public int getItemCount() {
            return mChatList.size();
        }
    }
    //asynctask that sends a post request
    private class PostTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            //builds request body with parameters
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("key", "27ccc630-b316-44d0-91b9-ffbef7cf3758")
                    .addFormDataPart("client", "&isaac.lewis@mymail.champlain.edu")
                    .addFormDataPart("message",currMessage)
                    .build();

            Request request = new Request.Builder()
                    .url("https://www.stepoutnyc.com/chitchat?key=27ccc630-b316-44d0-91b9-ffbef7cf3758&client=isaac.lewis@mymail.champlain.edu")
                    .post(requestBody)
                    .build();


            try (Response response = mHTTPClient.newCall(request).execute()) {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: Could not complete request.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("LOGS", result);
            //update with the message now posted. dont worry about doing it locally, just load everything again and it should be in order
            refresh();
        }


    }
    //asynctask that handles liking and disliking
    private class LikeDislikeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    //concatenates the like/dislike and message id into the url
                    .url("https://www.stepoutnyc.com/chitchat/" + currLDL + "/" + currId + "?key=27ccc630-b316-44d0-91b9-ffbef7cf3758&client=isaac.lewis@mymail.champlain.edu")
                    .build();


            try (Response response = mHTTPClient.newCall(request).execute()) {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: Could not complete request.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("CHIT", result);
            refresh();

        }


    }
    //parses the json object and sets up a chat object
    private void parseJSON(String jsonString) {
        mChatList.clear();
        try {
            JSONObject chats = new JSONObject(jsonString);
            JSONArray items = chats.getJSONArray("messages");
            for (int i = 0; i < items.length(); i++) {
                JSONObject chat = items.getJSONObject(i);
                String client = chat.getString("client");
                String date = chat.getString("date");
                int likes = chat.getInt("likes");
                int dislikes = chat.getInt("dislikes");
                String message = chat.getString("message");
                String _id = chat.getString("_id");
                Chat current = new Chat();
                current.setUser(client);
                current.setDate(date);
                current.setmLikes(likes);
                current.setmDislikes(dislikes);
                current.setmContent(message);
                current.set_id(_id);
                //adds new chat to the list
                mChatList.add(current);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //asynctask to refresh, just gets a new json from the server basically
    private class RefreshTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            Request request = new Request.Builder()
                    .url("https://www.stepoutnyc.com/chitchat?key=27ccc630-b316-44d0-91b9-ffbef7cf3758&client=isaac.lewis@mymail.champlain.edu")
                    .build();


            try (Response response = mHTTPClient.newCall(request).execute()) {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
                return "Error: Could not complete request.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("CHIT", result);
            parseJSON(result);
            mAdapter.notifyDataSetChanged();
        }


    }
    //refreshes the feed
    private void refresh() {
        RefreshTask rt = new RefreshTask();
        rt.execute();
    }
    //"What this is about isnt winning or losing. its about sending a message"
    private void sendMessage(String message){
        PostTask pt = new PostTask();
        pt.execute();
    }
    //likes a message
    private void like(String id){
        currId = id;
        currLDL = "like";
        LikeDislikeTask ldt = new LikeDislikeTask();
        ldt.execute();
    }
    //dislikes a message
    private void dislike(String id){
        currId = id;
        currLDL = "dislike";
        LikeDislikeTask ldt = new LikeDislikeTask();
        ldt.execute();
    }

}