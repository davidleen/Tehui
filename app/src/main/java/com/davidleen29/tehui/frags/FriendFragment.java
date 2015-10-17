package com.davidleen29.tehui.frags;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.davidleen29.tehui.R;
import com.davidleen29.tehui.acts.AddMessageActivity;
import com.davidleen29.tehui.acts.NewMessageActivity;
import com.davidleen29.tehui.acts.RegisterActivity;
import com.davidleen29.tehui.acts.UserMessageListActivity;
import com.davidleen29.tehui.acts.MessageDetailActivity;
import com.davidleen29.tehui.adapters.FriendChatAdapter;
import com.davidleen29.tehui.api.ApiManager;
import com.davidleen29.tehui.events.AppInitCompleteEvent;
import com.davidleen29.tehui.events.CitySetEvent;
import com.davidleen29.tehui.events.MessageSendEvent;
import com.davidleen29.tehui.events.UserInfoRefreshEvent;
import com.davidleen29.tehui.helper.CacheManager;
import com.davidleen29.tehui.helper.FootViewHelper;
import com.davidleen29.tehui.helper.ImageLoaderHelper;
import com.davidleen29.tehui.tasks.ThTask;
import com.davidleen29.tehui.utils.ToastUtils;
import com.davidleen29.tehui.widget.HeaderView;
import com.google.inject.Inject;
import com.huiyou.dp.service.protocol.Circle;
import com.huiyou.dp.service.protocol.Common;
import com.huiyou.dp.service.protocol.User;
import com.nostra13.universalimageloader.core.ImageLoader;


import roboguice.inject.InjectView;


public class FriendFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.listView)
    ListView list;

    @InjectView(R.id.header)
    HeaderView header;

    @Inject
    FriendChatAdapter  adapter;


    @Inject
    ApiManager apiManager;


    @Inject
    CacheManager cacheManager;

    @InjectView(R.id.swipe_container)
    SwipeRefreshLayout refreshLayout;





      View  listHeader;

    FootViewHelper footViewHelper;


    private User.UserSetUserInfo userSetUserInfo;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendFragment newInstance(String param1, String param2) {
        FriendFragment fragment = new FriendFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);





          footViewHelper=new FootViewHelper(getActivity(), new FootViewHelper.PageListener() {
            @Override
            public void onPageChanged(int newPageIndex, int pageCount) {

                loadData(header.getCurrentHeadTabIndex(),newPageIndex,pageCount);
            }
        });


        listHeader=getLayoutInflater(null).inflate(R.layout.list_header_friend_chat,null);
        listHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), NewMessageActivity.class);
                startActivity(intent);

                list.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        list.removeHeaderView(listHeader);
                    }
                },1000);
            }
        });
        list.addHeaderView(listHeader);
        list.addFooterView(footViewHelper.getView());
        list.setAdapter(adapter);
        list.removeHeaderView(listHeader);
        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {




            if(firstVisibleItem+visibleItemCount>=totalItemCount )

                footViewHelper.becomeVisible();
            }
        });



        header.setOnTabClickListener(new HeaderView.OnTabClickListener() {
            @Override
            public void onTabClick(View[] tabs, int clickIndex) {
                onRefresh();

            }
        });



      //  header.setLeftIconUrl(cacheManager.getCurrentUser().get);
        header.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent=new Intent(getActivity(),UserMessageListActivity.class);
                intent.putExtra(UserMessageListActivity.EXTRA_USER_ID,cacheManager.getCurrentUser().getUserId());
                intent.putExtra(UserMessageListActivity.EXTRA_USER_NAME,userSetUserInfo==null?cacheManager.getCurrentUser().getUserName():userSetUserInfo.getNickName());
                startActivityForResult(intent, -1);

            }
        });

        header.setOnRight1ClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cacheManager.getCurrentUser().getUserType().equals(Common.UserType.UserType_TEMP)) {



                    BaseDialogFragment dialogFragment=   new BaseDialogFragment()
                    {

                        @Override
                        protected void onYesClick(View view) {
                            dismiss();
                            Intent intent = new Intent(getActivity(), RegisterActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        protected void onNoClick(View view) {

                            dismiss();
                        }
                    };
                    dialogFragment.setParams(null, "您当前身份是游客，不能发惠友圈消息，马上注册？", "好的", "我再看看");

                    dialogFragment.show(getChildFragmentManager(),dialogFragment.getClass().getName());


                    return;
                }

                Intent intent = new Intent(getActivity(), AddMessageActivity.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.EXTRA_MESSAGE_ID, ((Circle.CircleMessage) parent.getItemAtPosition(position)).getMessageId());
                startActivity(intent);
            }
        });


        adapter.setMessageListListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Circle.CircleMessage tag = (Circle.CircleMessage) v.getTag();

                Intent intent = new Intent(getActivity(), UserMessageListActivity.class);
                intent.putExtra(UserMessageListActivity.EXTRA_USER_ID, tag.getUserId());
                intent.putExtra(UserMessageListActivity.EXTRA_USER_NAME, tag.getUserNickName());
                startActivity(intent);

            }
        });

        adapter.setCommentListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Circle.CircleMessage tag = (Circle.CircleMessage) v.getTag();

                Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
                intent.putExtra(MessageDetailActivity.EXTRA_MESSAGE_ID, tag.getMessageId() );
                intent.putExtra(MessageDetailActivity.EXTRA_ASK_FOR_COMMENT,true);
                startActivity(intent);


            }
        });

        if(cacheManager.hasInit())

            header.performTabClick(0);

    }



    private void loadData(int index)
    {
        loadData(index,0, 10);
    }

    private void loadData(int index, final int pageIndex, final int pageCount)
    {

        Common.CircleMessageType type= Common.CircleMessageType.CircleMessageType_ALL;
        switch (index)
        {
            case 0:type=Common.CircleMessageType.CircleMessageType_ALL; break;
            case 1:type=Common.CircleMessageType.CircleMessageType_FIND_CARD; break;
            case 2:type=Common.CircleMessageType.CircleMessageType_FIND_DATE; break;
        }
        final Common.CircleMessageType finalType = type;
        new ThTask<Circle.GetMessagesResponse>(getActivity(),true) {
            @Override
            public Circle.GetMessagesResponse call() throws Exception {
                return apiManager.getMessages(finalType,  pageIndex,pageCount);
            }

            @Override
            protected void doOnSuccess(Circle.GetMessagesResponse data) {

                if(refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
                if(data.getErrCode()== Circle.GetMessagesResponse.ErrorCode.ERR_OK)
                {


                    if(pageIndex==0)
                        adapter.setDataArray(data.getMessageList());
                    else
                        adapter.addDataArray(data.getMessageList());

                    footViewHelper.setInfo(pageIndex,pageCount,data.getTotalCount());




                }
                else
                {
                    ToastUtils.show(data.getErrMsg());
                }

            }
        }.execute();
        refreshLayout.setRefreshing(true);

    }


    public void onEvent(MessageSendEvent messageSendEvent)
    {

         header.performTabClick(0);

    }


    public void onEvent(CitySetEvent citySetEvent)
    {

        header.performTabClick(0);

    }


    public void onEvent(UserInfoRefreshEvent userInfoRefreshEvent)
    {
        this.userSetUserInfo=       userInfoRefreshEvent.userSetUserInfo;
        header.setLeftIconUrl(userInfoRefreshEvent.userSetUserInfo.getHeadImg());
        ImageLoader.getInstance().displayImage(userInfoRefreshEvent.userSetUserInfo.getHeadImg(),(ImageView)(listHeader.findViewById(R.id.userPhoto)), ImageLoaderHelper.getPortraitDisplayOptions());

    }
    public void onEvent(   AppInitCompleteEvent event)
    {
        header.performTabClick(0);
    }

    @Override
    public void onRefresh() {
       loadData(header.getCurrentHeadTabIndex());
        loadNewMessageCount();
    }

    private void loadNewMessageCount() {


        new ThTask<Circle.GetNewRepliesCountResponse>(getActivity(),true) {
            @Override
            public Circle.GetNewRepliesCountResponse call() throws Exception {
                return apiManager.getNewRepliesCount(cacheManager.getCurrentUser().getUserId());
            }

            @Override
            protected void doOnSuccess(Circle.GetNewRepliesCountResponse data) {


                if(data.getErrCode()==Circle.GetNewRepliesCountResponse.ErrorCode.ERR_OK)
                {

                    if(data.getRepliesCount()==0)
                        list.removeHeaderView(listHeader);
                    else {

                        if(list.getHeaderViewsCount()==0)
                            list.addHeaderView(listHeader);
                    }
                    ((TextView) listHeader.findViewById(R.id.message)).setText("您有"+data.getRepliesCount()+"条新消息");


                }else
                {



                    switch (data.getErrCode().getNumber())
                    {


                        default:
                            ToastUtils.show(data.getErrMsg());
                    }
                }



            }
        }.execute();


    }



}
