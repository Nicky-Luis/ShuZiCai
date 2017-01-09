package com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview;

public interface MultiItemTypeSupport<T>
{
	int getLayoutId(int position, T t);
	
	int getViewTypeCount();
	
	int getItemViewType(int postion, T t);
}