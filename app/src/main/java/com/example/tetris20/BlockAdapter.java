package com.example.tetris20;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class BlockAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    public int movingBlocks[][] = new int[15][10];
    public int fixedBlocks[][] = new int[15][10];
    public int allBlocks[][] = new int[15][10];
    public int centerX, centerY;


    public BlockAdapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 150;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView imageView;
    }

    int[] colors  = {Color.WHITE, Color.BLUE};
    int[] colors2  = {Color.WHITE, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK};

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int x = position/10;
        int y = position%10;

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.layout_grid_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //values
        if(movingBlocks[x][y] == 1)
            holder.imageView.setBackgroundColor(colors[movingBlocks[x][y]]);
        else if(fixedBlocks[x][y] == 1)
            holder.imageView.setBackgroundColor(colors2[fixedBlocks[x][y]]);
        else
            holder.imageView.setBackgroundColor(colors2[fixedBlocks[x][y]]);
//
//        if(x == centerX && y == centerY)
//            holder.imageView.setBackgroundColor(Color.GRAY);

        return convertView;
    }

    public void addBlocks(){
        for (int i = 0; i < allBlocks.length; i++)
            for (int j = 0; j < allBlocks[0].length; j++) {
                allBlocks[i][j] = movingBlocks[i][j] + fixedBlocks[i][j];
            }
    }
}
