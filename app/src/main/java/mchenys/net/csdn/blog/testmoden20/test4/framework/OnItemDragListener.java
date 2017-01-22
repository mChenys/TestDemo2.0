package mchenys.net.csdn.blog.testmoden20.test4.framework;

public interface OnItemDragListener {
    /**
     * 当item交换位置的时候回调的方法，我们只需要在该方法中实现数据的交换即可
     *
     * @param from 开始的position
     * @param to   拖拽到的position
     */
    void onChange(int from, int to);


    /**
     * 设置某个item隐藏,同时调用notifyDataSetChanged刷新
     *
     * @param hidePosition
     */
    void setItemHideAndNotify(int hidePosition);


}
