package com.zchun.chapter15;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Michael-Scott
 * 非阻塞队列算法中的插入（Michael与Scott, 1996）
 * @author chun.zhou
 * @date 2018/8/3 9:39
 */
public class LinkedQueue<E> {
    private static class Node<E> {
        final E item;
        final AtomicReference<Node<E>> next;
        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<Node<E>>(next);
        }
    }

    private final Node<E> dummy = new Node<E>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<Node<E>>(dummy);

    public boolean put(E item) {
        Node<E> newNode = new Node<E>(item, null);
        while(true) {
            Node<E> curTail = tail.get();
            Node<E> tailNext = curTail.next.get();
            //步骤A 检查队列是否处于中间状态
            if (tailNext != null) {
                //队列处于静止状态，推进尾节点
                //步骤B
                tail.compareAndSet(curTail, tailNext);
            } else {
                //处于静止状态，尝试插入新节点
                //步骤C
                if (curTail.next.compareAndSet(null, newNode)) {
                    //插入成功，尝试推进尾节点
                    //步骤D
                    tail.compareAndSet(curTail, newNode);
                    return true;
                }
            }
        }
    }
}
