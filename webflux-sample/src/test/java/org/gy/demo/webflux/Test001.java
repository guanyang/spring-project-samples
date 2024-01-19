package org.gy.demo.webflux;

import java.io.Serial;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Test001 {

    // 1. 两数之和
    //https://leetcode.cn/problems/two-sum/description/?envType=study-plan-v2&envId=top-100-liked
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return new int[0];
        }
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer tmp = map.get(target - nums[i]);
            if (tmp != null) {
                return new int[]{i, tmp};
            } else {
                map.put(nums[i], i);
            }
        }
        return new int[0];
    }

    //49.字母异位词分组
    //https://leetcode.cn/problems/group-anagrams/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {
            char[] array = str.toCharArray();
            Arrays.sort(array);
            String key = new String(array);
            List<String> strings = map.computeIfAbsent(key, k -> new ArrayList<String>());
            strings.add(str);
        }
        return new ArrayList<>(map.values());
    }

    //128.最长连续序列
    //https://leetcode.cn/problems/longest-consecutive-sequence/description/?envType=study-plan-v2&envId=top-100-liked
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        //key:nums[i] value:在数组中连续数字的长度
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                continue;
            }
            //当前数字的左边连续数字的长度，表示区间[num-left,num-1]
            int left = map.getOrDefault(nums[i] - 1, 0);
            //当前数字的右边连续数字的长度，表示区间[num+1,num+right]
            int right = map.getOrDefault(nums[i] + 1, 0);
            int curLen = left + right + 1;
            max = Math.max(max, curLen);
            //更新当前数字的连续数字的长度
            map.put(nums[i], curLen);
            //更新左边连续数字的长度
            if (left > 0) {
                map.put(nums[i] - left, curLen);
            }
            //更新右边连续数字的长度
            if (right > 0) {
                map.put(nums[i] + right, curLen);
            }
        }
        return max;
    }

    //283.移动零
    //https://leetcode.cn/problems/move-zeroes/description/?envType=study-plan-v2&envId=top-100-liked
    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length == 0) {
            return;
        }
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                swap(nums, i, j);
                j++;
            }
        }
    }

    //11.盛最多水的容器
    //https://leetcode.cn/problems/container-with-most-water/description/?envType=study-plan-v2&envId=top-100-liked
    public int maxArea(int[] height) {
        if (height == null || height.length == 0) {
            return 0;
        }
        int max = 0;
        int i = 0, j = height.length - 1;
        while (i < j) {
            //计算面积
            int area = Math.min(height[i], height[j]) * (j - i);
            max = Math.max(max, area);
            //将短板向中间移动
            if (height[i] < height[j]) {
                i++;
            } else {
                j--;
            }
        }
        return max;
    }

    //15.三数之和
    //https://leetcode.cn/problems/3sum/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        //数组长度小于3，直接返回结果
        if (nums == null || nums.length < 3) {
            return result;
        }
        //排序
        Arrays.sort(nums);
        int len = nums.length;
        for (int i = 0; i < len; i++) {
            //因为已经排序好，所以后面不可能有三个数加和等于0，直接返回结果
            if (nums[i] > 0) {
                return result;
            }
            //对于重复元素，跳过
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1, right = len - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[left]);
                    list.add(nums[right]);
                    result.add(list);
                    //跳过重复元素
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    //
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < 0) {
                    //说明左边元素太小了，向右移动
                    left++;
                } else {
                    //说明右边元素太大了，向左移动
                    right--;
                }
            }
        }
        return result;
    }

    //42.接雨水
    //https://leetcode.cn/problems/trapping-rain-water/description/?envType=study-plan-v2&envId=top-100-liked
    public int trap(int[] height) {
        // 如果山脉数组为null或长度为0，则返回0
        if (height == null || height.length == 0) {
            return 0;
        }
        int sum = 0; // 集水区的面积和
        int left = 0, right = height.length - 1; // 左右指针
        int leftMax = 0, rightMax = 0; // 左右两边的最大高度
        // 从左到右遍历山脉数组
        while (left < right) {
            leftMax = Math.max(leftMax, height[left]); // 更新左边最大高度
            rightMax = Math.max(rightMax, height[right]); // 更新右边最大高度
            // 如果当前左边的高度小于右边的高度
            if (height[left] < height[right]) {
                sum += leftMax - height[left]; // 集水区的面积增加左边的高度差
                left++; // 左指针右移
            } else {
                sum += rightMax - height[right]; // 集水区的面积增加右边的高度差
                right--; // 右指针左移
            }
        }
        return sum; // 返回最大集水区的面积
    }

    //3.无重复字符的最长子串
    //https://leetcode.cn/problems/longest-substring-without-repeating-characters/description/?envType=study-plan-v2&envId=top-100-liked
    public int lengthOfLongestSubstring(String s) {
        // 如果输入字符串为空或为空字符串，则返回0
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int max = 0;
        int start = 0;
        //记录不重复字符串的索引
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            Integer index = map.get(s.charAt(i));
            if (index != null) {
                start = Math.max(start, index + 1);
            }
            map.put(s.charAt(i), i);
            max = Math.max(max, i - start + 1);
        }
        // 返回最大长度
        return max;
    }

    //438.找到字符串中所有字母异位词
    //https://leetcode.cn/problems/find-all-anagrams-in-a-string/description/?envType=study-plan-v2&envId=top-100-liked
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }
        Map<Character, Integer> need = new HashMap<>(p.length());
        Map<Character, Integer> window = new HashMap<>(p.length());
        for (int i = 0; i < p.length(); i++) {
            need.put(p.charAt(i), need.getOrDefault(p.charAt(i), 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;

        while (right < s.length()) {
            char cur = s.charAt(right);
            right++;
            // 进行窗口内数据的一系列更新
            if (need.containsKey(cur)) {
                Integer total = window.getOrDefault(cur, 0) + 1;
                window.put(cur, total);
                if (window.get(cur).equals(need.get(cur))) {
                    valid++;
                }
            }
            // 判断左侧窗口是否要收缩
            while (right - left >= p.length()) {
                // 当窗口符合条件时，把起始索引加入 result 中
                if (valid == need.size()) {
                    result.add(left);
                }
                char d = s.charAt(left);
                left++;
                // 进行窗口数据当一系列更新
                if (need.containsKey(d)) {
                    if (window.get(d).equals(need.get(d))) {
                        valid--;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }

        return result;
    }

    //560.和为K的子数组
    //https://leetcode.cn/problems/subarray-sum-equals-k/description/?envType=study-plan-v2&envId=top-100-liked
    public int subarraySum(int[] nums, int k) {
        // 计算子数组和为 k 的数量
        int count = 0, pre = 0;
        HashMap<Integer, Integer> mp = new HashMap<>(); // 创建哈希表保存前缀和
        mp.put(0, 1); // 初始化哈希表，前缀和为 0 的数量为 1
        for (int i = 0; i < nums.length; i++) { // 遍历数组
            pre += nums[i]; // 更新前缀和
            if (mp.containsKey(pre - k)) { // 判断哈希表中是否存在前缀和之差 k
                count += mp.get(pre - k); // 若存在，则将该前缀和之差 k 的数量加到结果 count 中
            }
            mp.put(pre, mp.getOrDefault(pre, 0) + 1); // 更新哈希表，表示前缀和为 pre 的数量加 1
        }
        return count;
    }

    //160.相交链表
    //https://leetcode.cn/problems/intersection-of-two-linked-lists/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) return null;
        ListNode pA = headA, pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    //206.反转链表
    //https://leetcode.cn/problems/reverse-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
    public static ListNode reverseList(ListNode head) {
        ListNode cur = head, pre = null;
        while (cur != null) {
            ListNode tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }

    public static ListNode endOfFirstHalf(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    //234.回文链表
    //https://leetcode.cn/problems/palindrome-linked-list/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        // 找到前半部分链表的尾节点并反转后半部分链表
        ListNode firstHalfEnd = endOfFirstHalf(head);
        ListNode secondHalfStart = reverseList(firstHalfEnd.next);
        // 判断是否回文
        ListNode p1 = head;
        ListNode p2 = secondHalfStart;
        boolean isPalindrome = true;
        while (isPalindrome && p2 != null) {
            if (p1.val != p2.val) {
                isPalindrome = false;
            }
            p1 = p1.next;
            p2 = p2.next;
        }
        // 还原链表并返回结果
        firstHalfEnd.next = reverseList(secondHalfStart);
        return isPalindrome;
    }

    //141.环形链表
    //https://leetcode.cn/problems/linked-list-cycle/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean hasCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) { // 在指针移动后再比较，排除初始都指向头结点的情况
                return true;
            }
        }
        return false;
    }

    //142.环形链表 II
    //https://leetcode.cn/problems/linked-list-cycle-ii/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode detectCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (true) {
            if (fast == null || fast.next == null) return null;
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) break;
        }
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return fast;
    }

    //21.合并两个有序链表
    //https://leetcode.cn/problems/merge-two-sorted-lists/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }
        ListNode cur = new ListNode();
        ListNode dummyHead = cur;
        while (list1 != null && list2 != null) {
            if (list1.val < list2.val) {
                cur.next = list1;
                list1 = list1.next;
            } else {
                cur.next = list2;
                list2 = list2.next;
            }
            cur = cur.next;
        }
        if (list1 != null) {
            cur.next = list1;
        } else if (list2 != null) {
            cur.next = list2;
        }
        return dummyHead.next;
    }

    //2.两数相加
    //https://leetcode.cn/problems/add-two-numbers/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode cur = new ListNode();
        ListNode dummyHead = cur;
        int nextStep = 0;
        while (l1 != null || l2 != null) {
            int v1 = l1 == null ? 0 : l1.val;
            int v2 = l2 == null ? 0 : l2.val;
            int sum = v1 + v2 + nextStep;
            nextStep = sum / 10;
            cur.next = new ListNode(sum % 10);
            cur = cur.next;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        if (nextStep > 0) {
            cur.next = new ListNode(nextStep);
        }
        return dummyHead.next;
    }

    //19.删除链表的倒数第N个结点
    //https://leetcode.cn/problems/remove-nth-node-from-end-of-list/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null || n < 0) {
            return head;
        }
        ListNode dummyHead = new ListNode(0, head);
        ListNode slow = dummyHead, fast = dummyHead;
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }
        while (fast != null && fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }
        slow.next = slow.next.next;
        return dummyHead.next;
    }

    //24.两两交换链表中的节点
    //https://leetcode.cn/problems/swap-nodes-in-pairs/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummyHead = new ListNode(0, head);
        ListNode cur = dummyHead;
        while (cur.next != null && cur.next.next != null) {
            ListNode node1 = cur.next;
            ListNode node2 = cur.next.next;
            cur.next = node2;
            node1.next = node2.next;
            node2.next = node1;
            cur = node1;
        }
        return dummyHead.next;
    }

    //25.K个一组翻转链表
    //https://leetcode.cn/problems/reverse-nodes-in-k-group/description/?envType=study-plan-v2&envId=top-100-liked
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k < 2) {
            return head;
        }
        ListNode dummyHead = new ListNode(0, head);
        ListNode pre = dummyHead, end = dummyHead;
        while (end.next != null) {
            for (int i = 0; i < k && end != null; i++) {
                end = end.next;
            }
            if (end == null) {
                break;
            }
            // 保存链表
            ListNode start = pre.next;
            // 保存链表的下一个节点
            ListNode next = end.next;
            // 断开链表
            end.next = null;
            // 反转链表
            pre.next = reverseList(start);
            start.next = next;
            pre = start;
            end = start;
        }
        return dummyHead.next;
    }

    //146.LRU缓存机制
    //https://leetcode.cn/problems/lru-cache/description/?envType=study-plan-v2&envId=top-100-liked
    static class LRUCache extends LinkedHashMap<Integer, Integer> {

        @Serial
        private static final long serialVersionUID = 1279072177403868498L;

        private final int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75F, true);
            this.capacity = capacity;
        }

        public int get(int key) {
            return super.getOrDefault(key, -1);
        }

        public void put(int key, int value) {
            super.put(key, value);
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

    //101.对称二叉树
    //https://leetcode.cn/problems/symmetric-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        //调用递归函数，比较左节点，右节点
        return isSymmetric(root.left, root.right);
    }

    private static boolean isSymmetric(TreeNode left, TreeNode right) {
        //递归的终止条件是两个节点都为空
        //或者两个节点中有一个为空
        //或者两个节点的值不相等
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left.val != right.val) {
            return false;
        }
        //再递归的比较 左节点的左孩子 和 右节点的右孩子
        //以及比较  左节点的右孩子 和 右节点的左孩子
        return isSymmetric(left.left, right.right) && isSymmetric(left.right, right.left);
    }

    //102.二叉树的层序遍历
    //https://leetcode.cn/problems/binary-tree-level-order-traversal/description/?envType=study-plan-v2&envId=top-100-liked
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                list.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(list);
        }
        return result;
    }

    //108.将有序数组转换为二叉搜索树
    //https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/description/?envType=study-plan-v2&envId=top-100-liked
    public TreeNode sortedArrayToBST(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    public TreeNode helper(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        // 总是选择中间位置左边的数字作为根节点
        int mid = (left + right) / 2;

        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }

    private static Map<Character, Character> map = new HashMap<>();

    static {
        map.put(')', '(');
        map.put('}', '{');
        map.put(']', '[');
    }

    //20.有效的括号
    //https://leetcode.cn/problems/valid-parentheses/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean isValid(String s) {
        if (s == null || s.isEmpty() || s.length() % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (map.containsKey(c)) {
                if (stack.isEmpty() || stack.peek() != map.get(c)) {
                    return false;
                }
                stack.pop();
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    //139.单词拆分
    //https://leetcode.cn/problems/word-break/description/?envType=study-plan-v2&envId=top-100-liked
    public boolean wordBreak(String s, List<String> wordDict) {
        if (s == null || s.isEmpty() || wordDict == null || wordDict.isEmpty()) {
            return false;
        }
        int len = s.length();
        //dp[i] 表示字符串s的前i个字符能否被拆分成若干个单词出现在字典中
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        Set<String> set = new HashSet<>(wordDict);
        for (int i = 1; i <= len; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && set.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    //75.颜色分类
    //https://leetcode.cn/problems/sort-colors/description/?envType=study-plan-v2&envId=top-100-liked
    public void sortColors(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        int p0 = 0, i = 0, p2 = nums.length;
        while (p0 < p2) {
            if (nums[i] == 0) {
                swap(nums, i, p0);
                p0++;
                i++;
            } else if (nums[i] == 1) {
                i++;
            } else {
                p2--;
                swap(nums, i, p2);
            }
        }
    }

    //239.滑动窗口最大值
    //https://leetcode.cn/problems/sliding-window-maximum/description/?envType=study-plan-v2&envId=top-100-liked
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length < 2) return nums;
        // 双向队列 保存当前窗口最大值的数组位置 保证队列中数组位置的数值按从大到小排序
        Deque<Integer> queue = new LinkedList<>();
        // 结果数组
        int[] result = new int[nums.length - k + 1];
        // 遍历nums数组
        for (int i = 0; i < nums.length; i++) {
            // 保证从大到小 如果前面数小则需要依次弹出，直至满足要求
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[i]) {
                queue.pollLast();
            }
            // 添加当前值对应的数组下标
            queue.addLast(i);
            // 判断当前队列中队首的值是否有效
            if (queue.peek() <= i - k) {
                queue.poll();
            }
            // 当窗口长度为k时 保存当前窗口中最大值
            if (i + 1 >= k) {
                result[i + 1 - k] = nums[queue.peek()];
            }
        }
        return result;
    }

    //152.乘积最大子数组
    //https://leetcode.cn/problems/maximum-product-subarray/description/?envType=study-plan-v2&envId=top-100-liked
    public int maxProduct(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MIN_VALUE;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int len = nums.length;
        int max = nums[0],min = nums[0],result = nums[0];
        for (int i = 1; i < len; i++) {
            if (nums[i] > 0) {
                max = Math.max(max * nums[i], nums[i]);
                min = Math.min(min * nums[i], nums[i]);
            } else {
                int tmp = max;
                max = Math.max(min * nums[i], nums[i]);
                min = Math.min(tmp * nums[i], nums[i]);
            }
            result = Math.max(result, max);
        }
        return result;
    }

    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return Integer.MIN_VALUE;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int len = nums.length;
        int max = nums[0],result = nums[0];
        for (int i = 1; i < len; i++) {
            max = Math.max(max + nums[i], nums[i]);
            result = Math.max(result, max);
        }
        return result;
    }

    //300.最长递增子序列
    //https://leetcode.cn/problems/longest-increasing-subsequence/description/?envType=study-plan-v2&envId=top-100-liked
    //dp[i] 表示以nums[i]结尾的最长递增子序列的长度
    //dp[i] = max(dp[j]) + 1, j < i, nums[j] < nums[i]
    //dp[i] = 1, nums[i] > nums[j]
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return 1;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int max = 1;
        for (int i = 1; i < nums.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    //416.分割等和子集
    //https://leetcode.cn/problems/partition-equal-subset-sum/description/?envType=study-plan-v2&envId=top-100-liked
    /**
     * 判断给定数组是否可以划分为两个和相等的子集
     * @param nums 给定的整数数组
     * @return 若能划分为两个和相等的子集，返回true；否则返回false
     */
    public boolean canPartition(int[] nums) {
        if (nums == null || nums.length < 2) {
            return false; // 数组为空或长度小于2，无法划分子集
        }
        int sum = 0; // 数组元素的总和
        int max = 0; // 数组中最大的元素
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i]; // 计算数组元素的总和
            max = Math.max(max, nums[i]); // 更新最大元素的值
        }
        if (sum % 2 == 1) {
            return false; // 数组元素的总和为奇数，无法划分成和相等的子集
        }
        int target = sum / 2; // 子集的目标和
        if (max > target) {
            return false; // 数组中最大的元素大于目标和，无法划分成和相等的子集
        }
        boolean[][] dp = new boolean[nums.length][target + 1]; // 动态规划数组
        for (int i = 0; i < nums.length; i++) {
            dp[i][0] = true; // 初始化dp数组，dp[i][0]表示是否可以为空集合
        }
        dp[0][nums[0]] = true; // 更新dp数组，dp[0][nums[0]]表示是否可以包含第一个元素
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j >= nums[i]) {
                    dp[i][j] = dp[i - 1][j] | dp[i - 1][j - nums[i]]; // 更新dp数组，dp[i][j]表示是否可以包含第i个元素
                } else {
                    dp[i][j] = dp[i - 1][j]; // 更新dp数组，dp[i][j]表示是否可以包含第i个元素
                }
            }
        }
        return dp[nums.length - 1][target]; // 返回是否可以划分成和相等的子集
    }

    public boolean canPartitionKSubsets(int[] nums, int k) {
        if (nums == null || nums.length < k ){
            return false;
        }
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }
        if (sum % k!= 0) {
            return false;
        }
        int target = sum / k;
        boolean [][] dp = new boolean[nums.length][target + 1];
        for (int i = 0; i < nums.length; i++) {
            dp[i][0] = true;
        }
        for (int i = 1; i < nums.length; i++) {
            for (int j = 1; j <= target; j++) {
                if (j >= nums[i]) {
                    dp[i][j] = dp[i - 1][j] | dp[i - 1][j - nums[i]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[nums.length - 1][target];
    }

    //56. 合并区间
    //https://leetcode.cn/problems/merge-intervals/description/?envType=study-plan-v2&envId=top-100-liked
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length < 2) {
            return intervals;
        }
        Arrays.sort(intervals,new Comparator<int[]>(){
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            if (list.isEmpty()) {
                list.add(intervals[i]);
            } else {
                int left = intervals[i][0], right = intervals[i][1];
                int[] last = list.get(list.size() - 1);
                if (last[1] >= left) {
                    last[1] = Math.max(last[1], right);
                } else {
                    list.add(intervals[i]);
                }
            }
        }
        return list.toArray(new int[list.size()][]);
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int x, TreeNode left, TreeNode right) {
            val = x;
            this.left = left;
            this.right = right;
        }

        TreeNode(int x) {
            val = x;
        }


    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int x) {
            val = x;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
