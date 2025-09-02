package test.util.rmq;

import org.junit.jupiter.api.Test;

import io.github.timemachinelab.util.rmq.SegmentTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

@DisplayName("线段树测试")
public class SegmentTreeTest {
    
    @BeforeEach
    void setUp() {
        // 测试前的初始化
    }
    
    @Test
    @DisplayName("测试基本功能")
    void testBasicFunctionality() {
        List<Integer> list = List.of(1,3,4,2,5,10,6);
        SegmentTree<Integer,Integer> segmentTree = SegmentTree.forInt(list);
        System.out.println(segmentTree.max(0, 6));
        segmentTree.update(0, 20);
        System.out.println(segmentTree.max(0, 6));
        System.out.println(segmentTree.min(0, 6)); 
    }
}
