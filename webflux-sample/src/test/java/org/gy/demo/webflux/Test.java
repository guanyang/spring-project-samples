package org.gy.demo.webflux;

import java.util.*;

public class Test {

    public static Map<Character, Character> CHAR_MAP = new HashMap<>();

    static {
        CHAR_MAP.put(')', '(');
        CHAR_MAP.put('}', '{');
        CHAR_MAP.put(']', '[');
    }

    public static boolean checkString(String s) {
        if (s == null || s.length() % 2 == 1) {
            return false;
        }
        //定义栈，先进后出的结构
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (CHAR_MAP.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != CHAR_MAP.get(c)) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    public static final char LEFT = '(';
    public static final char RIGHT = ')';

    //状态转移方程推导
    //如果s[i-1] == '(', 那么dp[i] = dp[i-2] + 2;
    //如果s[i-1] == ')'且s[i-dp[i-1]-1] == '(', 那么dp[i] = dp[i-1] + dp[i-dp[i-1]-2] + 2;
    public int longestValidParentheses(String s) {
        int maxans = 0;
        int[] dp = new int[s.length()];
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == RIGHT) {
                if (s.charAt(i - 1) == LEFT) {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == LEFT) {
                    dp[i] = dp[i - 1] + ((i - dp[i - 1]) >= 2 ? dp[i - dp[i - 1] - 2] : 0) + 2;
                }
                maxans = Math.max(maxans, dp[i]);
            }
        }
        return maxans;
    }

    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(3);
        ListNode node5 = new ListNode(4);
        ListNode node6 = new ListNode(5);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;
        node5.next = node6;
        printListNode(node1);
        printListNode(deleteDuplicatesNew(node1));
    }

    public static void printListNode(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            System.out.print(cur.val + " ");
            cur = cur.next;
        }
        System.out.println();
    }

    public static String reverse(String str) {
        if (str == null || str.length() == 1) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }

    public static List<Integer> preScan(TreeNode root, List<Integer> list) {
        if (root == null) {
            return list;
        }
        list.add(root.val);
        preScan(root.left, list);
        preScan(root.right, list);
        return list;
    }

    public static List<Integer> inScan(TreeNode root, List<Integer> list) {
        if (root == null) {
            return list;
        }
        inScan(root.left, list);
        list.add(root.val);
        inScan(root.right, list);
        return list;
    }
    public static List<Integer> postScan(TreeNode root, List<Integer> list) {
        if (root == null) {
            return list;
        }
        postScan(root.left, list);
        postScan(root.right, list);
        list.add(root.val);
        return list;
    }

    /* 层序遍历 */
    List<Integer> levelOrder(TreeNode root) {
        // 初始化队列，加入根节点
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        // 初始化一个列表，用于保存遍历序列
        List<Integer> list = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll(); // 队列出队
            list.add(node.val);           // 保存节点值
            if (node.left != null)
                queue.offer(node.left);   // 左子节点入队
            if (node.right != null)
                queue.offer(node.right);  // 右子节点入队
        }
        return list;
    }

    public static ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode pre = dummy;
        ListNode end = dummy;

        while (end.next != null) {
            for (int i = 0; i < k && end != null; i++) {
                end = end.next;
            }
            if (end == null) break;
            ListNode start = pre.next;
            ListNode next = end.next;
            end.next = null;
            pre.next = reverseList(start);
            start.next = next;
            pre = start;

            end = pre;
        }
        return dummy.next;
    }

    public static ListNode reverseList(ListNode head) {
        ListNode cur = head, pre = null;
        while(cur != null) {
            ListNode tmp = cur.next; // 暂存后继节点 cur.next
            cur.next = pre;          // 修改 next 引用指向
            pre = cur;               // pre 暂存 cur
            cur = tmp;               // cur 访问下一节点
        }
        return pre;
    }


    //1-->2-->3-->4
    //2-->1-->4-->3
    //两两交换链表中的节点
    public static ListNode swapPairs(ListNode head) {
        //定义一个虚拟头节点
        ListNode pre = new ListNode(0,head);
        ListNode cur = pre;
        while(cur.next != null && cur.next.next != null) {
            ListNode node1 = cur.next;
            ListNode node2 = cur.next.next;
            //交换node1和node2
            cur.next = node2;
            node1.next = node2.next;
            node2.next = node1;
            //更新cur到下一个要交换的节点
            cur = node1;
        }
        return pre.next;
    }

    public static ListNode deleteDuplicatesNew(ListNode head) {
        if (head == null) {
            return head;
        }
        ListNode dummy = new ListNode(0, head);
        ListNode cur = dummy;
        while (cur.next != null && cur.next.next != null) {
            if (cur.next.val == cur.next.next.val) {
                int x = cur.next.val;
                while (cur.next != null && cur.next.val == x) {
                    cur.next = cur.next.next;
                }
            } else {
                cur = cur.next;
            }
        }
        return dummy.next;
    }

    public static ListNode deleteDuplicates(ListNode head) {
        ListNode cur = head;
        while(cur != null && cur.next != null) {
            if(cur.val == cur.next.val) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }
}
