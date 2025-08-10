package io.github.timemachinelab.util.rmq;

import io.github.timemachinelab.util.math.MathUtil;

import java.util.Comparator;
import java.util.List;

/**
 * segment tree
 * A segment tree can implement operations such as single-point updates, range updates, and range queries
 * (including range sum, range maximum, and range minimum) with o(logN)
 * @param <T>
 */
public class SegmentTree<T,V> implements RMQ<V>{

    private StNode<V>[] st;

    private final static int ROOT_IDX = 1;

    private int len;

    private final ValueCalculation<T,V> vc;

    private final Comparator<V> gtCp;

    private final Comparator<V> ltCp;

    public SegmentTree(List<T> list, ValueCalculation<T,V> vc, Comparator<V> gtCp) {
        if(list == null || list.isEmpty()){
            throw new IllegalArgumentException("list is empty");
        }
        if(vc == null || gtCp == null){
            throw new IllegalArgumentException("valueCalculation or Comparator is null");
        }
        this.vc = vc;
        this.gtCp = gtCp;
        this.ltCp = gtCp.reversed();
        this.len = list.size();
        this.st = new StNode[len<<2];
        buildTree(list, 0, len-1, ROOT_IDX);
    }

    private void buildTree(List<T> list, int l, int r, int idx){
        if(l == r){
            st[idx] = new StNode<>(vc.calculate(list.get(l)));
        }
        int mid = MathUtil.mid(l, r);
        buildTree(list, l, mid, leftIdx(idx));
        buildTree(list, mid+1, r, rightIdx(idx));
        upperNode(idx);
    }

    public static SegmentTree<Integer,Integer> forInt(List<Integer> list){
        return new SegmentTree<>(list, (ValueCalculation<Integer, Integer>) (t)-> t, Comparator.naturalOrder());
    }

    public static SegmentTree<Long,Long> forLong(List<Long> list){
        return new SegmentTree<>(list, (ValueCalculation<Long, Long>) (t)-> t, Comparator.naturalOrder());
    }

    private void upperNode(int idx){
        StNode<V> leftNode = st[leftIdx(idx)];
        StNode<V> rightNode = st[rightIdx(idx)];
        
        V max = gtCp.compare(leftNode.maxValue, rightNode.maxValue) > 0
                ? leftNode.maxValue : rightNode.maxValue;

        V min = ltCp.compare(leftNode.minValue, rightNode.minValue) > 0
                ? leftNode.minValue : rightNode.minValue;

        st[idx] = new StNode<>(min, max);
    }

    @Override
    public V max(int l, int r) {
        int idx = queryRange(ROOT_IDX, 0, len - 1, l, r, true);
        if(idx == -1){
            return null;
        }
        return st[idx].maxValue;
    }

    @Override
    public V min(int l, int r) {
        int idx = queryRange(ROOT_IDX, 0, len - 1, l, r, false);
        if(idx == -1){
            return null;
        }
        return st[idx].minValue;
    }

    private int queryRange(int idx, int x, int y, int l, int r, boolean isMax) {
        if(l > r || l < 0 || r >= len){
            return -1;
        }
        if(x>=l && r>=y){
            return idx;
        }
        int mid = MathUtil.mid(x, y);
        int lIdx = -1; int rIdx = -1;

        if(mid<=l){
            lIdx = queryRange(leftIdx(idx), x, mid, l, r, isMax);
        }
        if(r>mid){
            rIdx = queryRange(rightIdx(idx), mid+1, y, l, r, isMax);
        }

        if (lIdx == -1) return rIdx;
        if (rIdx == -1) return lIdx;

        StNode<V> leftNode = st[lIdx];
        StNode<V> rightNode = st[rIdx];

        if (isMax) {
            return gtCp.compare(leftNode.maxValue, rightNode.maxValue) > 0
                    ? lIdx : rIdx;
        } else {
            return ltCp.compare(leftNode.minValue, rightNode.minValue) > 0
                    ? lIdx : rIdx;
        }
    }

    public boolean update(int index, V value){
        if(index < 0 || index >= len){
            return false;
        }
        update(ROOT_IDX, index, 0, len, value);
        return true;
    }

    private void update(int idx, int l, int r, int index, V value){
        if(l==r){
            st[idx].setValue(value);
            return;
        }

        int mid = MathUtil.mid(index, r);
        if(index<=mid){
            update(leftIdx(idx), l, mid, index, value);
        }else{
            update(rightIdx(idx), mid+1, r, index, value);
        }
        upperNode(idx);
    }

    private int leftIdx(int idx){
        return idx << 1;
    }

    private int rightIdx(int idx){
        return idx << 1 | 1;
    }

    protected class StNode<V>{

        private V maxValue;

        private V minValue;

        protected StNode(V value){
            maxValue = minValue = value;
        }

        protected StNode(V minValue, V maxValue){
            this.minValue = minValue;
            this.maxValue = maxValue;
        }

        protected V value(){
            return maxValue;
        }

        protected void setValue(V value){
            this.maxValue = this.minValue = value;
        }

        protected void setMaxValue(V maxValue){
            this.maxValue = maxValue;
        }

        protected void setMinValue(V minValue){
            this.minValue = minValue;
        }

    }
}
