package org.gy.demo.webflux;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Test002 {

    public static void main(String[] args) {
        int[] nums = randomIntArray(10, 200);
        printArray(nums);
//        bubbleSort(nums);
//        quickSort(nums, 0, nums.length - 1);
        mergeSort(nums, 0, nums.length - 1);
//        heapSort(nums);
        printArray(nums);

//        String version1 = "0.1";
//        String version2 = "1.1";
//        System.out.println(compareVersion(version1, version2));
//        System.out.println("1".compareTo(""));Ï

        int[][] matrix = new int[][]{{0, 1, 0, 0}, {0, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 1, 0}};

        int n = matrix.length;
        boolean[] rows = new boolean[n];
        boolean[] cols = new boolean[n];
        boolean[] lefts = new boolean[2 * n - 1];
        boolean[] rights = new boolean[2 * n - 1];

        boolean flag = dfs(0, n, matrix, rows, cols, lefts, rights);

        if (flag) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
        Set<Integer> set = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Object[] array = set.toArray();

        char c = '.';
        System.out.println((int)'a');
        System.out.println((int)'0');
        System.out.println((int) c);
        System.out.println(c-'0');
        System.out.println(c-'a');

        Queue<int[]> queue = new PriorityQueue<>(2, (o1, o2) -> o1[0] - o2[0]);

    }

    private static boolean dfs(int row, int n, int[][] matrix, boolean[] rows, boolean[] cols, boolean[] lefts,
        boolean[] rights) {
        if (row >= n || row < 0) {
            return false;
        }
        //遍历当前行
        for (int i = 0; i < n; i++) {
            if (matrix[row][i] == 0) {
                continue;
            }
            //计算左右对角线
            int left = row - i + n - 1;
            int right = row + i;
            //判断是否已经存在
            if (rows[row] || cols[i] || lefts[left] || rights[right]) {
                return true;
            }
            rows[row] = cols[i] = lefts[left] = rights[right] = true;
        }
        //递归遍历下一行
        return dfs(row + 1, n, matrix, rows, cols, lefts, rights);
    }

    /**
     * 使用堆排序算法对数组进行排序。
     *
     * @param nums 待排序的整型数组。
     */
    public static void heapSort(int[] nums) {
        // 建立最大堆，从最后一个非叶子节点开始向上调整
        for (int i = nums.length / 2 - 1; i >= 0; i--) {
            siftDown(nums, nums.length, i);
        }
        // 依次从堆中取出最大元素，放至数组末尾，然后重新调整堆
        for (int i = nums.length - 1; i > 0; i--) {
            swap(nums, 0, i);
            siftDown(nums, i, 0);
        }
    }


    /**
     * 自顶向下堆化处理 该方法通过比较父节点和其子节点的值，将较大的元素下沉，以维持堆的性质。
     *
     * @param nums 堆数组
     * @param len 数组的有效长度
     * @param i 当前需要堆化的节点索引
     */
    public static void siftDown(int[] nums, int len, int i) {
        while (true) {
            int left = 2 * i + 1; // 左子节点索引
            int right = 2 * i + 2; // 右子节点索引
            int max = i; // 当前最大值节点索引，默认为父节点

            // 检查左子节点，如果存在且大于父节点，则更新最大值节点索引
            if (left < len && nums[left] > nums[max]) {
                max = left;
            }

            // 检查右子节点，如果存在且大于当前最大值节点，则更新最大值节点索引
            if (right < len && nums[right] > nums[max]) {
                max = right;
            }

            // 如果当前节点已经是最大值，或者左右子节点均越界，则结束循环
            if (max == i) {
                break;
            }

            // 交换当前节点与其较大的子节点，继续下沉调整
            swap(nums, i, max);
            i = max;
        }
    }


    public static int compareVersion(String version1, String version2) {
        if (version1 == null && version2 == null) {
            return 0;
        }
        if (version1 == null) {
            return -1;
        }
        if (version2 == null) {
            return 1;
        }
        if (version1.equals(version2)) {
            return 0;
        }
        String[] arr1 = version1.split("\\.");
        String[] arr2 = version2.split("\\.");
        int i = 0, j = 0;
        while (i < arr1.length || j < arr2.length) {
            int v1 = i < arr1.length ? Integer.parseInt(arr1[i]) : 0;
            int v2 = j < arr2.length ? Integer.parseInt(arr2[j]) : 0;
            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            } else {
                i++;
                j++;
            }
        }
        return 0;
    }

    private static int[] parse(String[] arr, int length) {
        int len = arr.length;
        int max = Math.max(len, length);
        int[] versions = new int[max];
        for (int i = 0; i < len; i++) {
            versions[i] = Integer.parseInt(arr[i]);
        }
        return versions;
    }

    public static void insertSort(int[] nums) {
        for (int i = 1; i < nums.length; i++) {
            // 先暂存这个元素，然后之前元素逐个后移，留出空位
            int temp = nums[i];
            int j = i;
            while (j > 0 && nums[j - 1] > temp) {
                nums[j] = nums[j - 1];
                j--;
            }
            nums[j] = temp;
        }
    }


    /**
     * 实现归并排序算法。 对给定的整型数组 nums 中指定范围 [low, high] 的元素进行排序。
     *
     * @param nums 待排序的整型数组。
     * @param low 排序范围的起始索引。
     * @param high 排序范围的终止索引。
     */
    public static void mergeSort(int[] nums, int low, int high) {
        // 当 low 小于 high 时，表示仍有元素需要排序
        if (low < high) {
            // 计算中间元素的索引，用于将数组划分为两部分
            int mid = low + ((high - low) >> 1);

            // 对数组的左半部分进行递归排序
            mergeSort(nums, low, mid);
            // 对数组的右半部分进行递归排序
            mergeSort(nums, mid + 1, high);

            // 如果数组的这个子区间本身有序，无需合并
            if (nums[mid] <= nums[mid + 1]) {
                return;
            }
            // 将排序好的左右两部分合并
            merge(nums, low, mid, high);
        }
    }

    /**
     * 将数组的两个部分合并成一个有序部分。
     *
     * @param nums 要合并的原始数组。
     * @param low 合并范围的起始索引。
     * @param mid 合并范围的中间索引，即左半部分的结束索引。
     * @param high 合并范围的结束索引。
     */
    private static void merge(int[] nums, int low, int mid, int high) {
        // 创建一个临时数组来存储合并后的结果。
        int[] tmp = new int[high - low + 1];
        int i = low, j = mid + 1, k = 0;

        // 遍历两个部分，将较小的元素依次放入临时数组。
        while (i <= mid && j <= high) {
            if (nums[i] <= nums[j]) {
                tmp[k++] = nums[i++];
            } else {
                tmp[k++] = nums[j++];
            }
        }

        // 处理左半部分剩余的元素。
        while (i <= mid) {
            tmp[k++] = nums[i++];
        }

        // 处理右半部分剩余的元素。
        while (j <= high) {
            tmp[k++] = nums[j++];
        }

        // 将合并后的结果复制回原始数组。
        for (int m = 0; m < tmp.length; m++) {
            nums[low + m] = tmp[m];
        }
    }

    /**
     * 查找数组中第 k 大的元素
     *
     * @param nums 整型数组，要查找的数组
     * @param k 查找的顺序，即第 k 大的元素
     * @return 返回数组中第 k 大的元素值
     */
    public static int findKthLargest(int[] nums, int k) {
        int n = nums.length; // 数组长度
        // 使用快速选择算法查找第 k 大的元素，调整范围为数组的整个长度
        return quickSelect(nums, 0, n - 1, n - k);
    }

    /**
     * 使用快速选择算法在数组中找到第k小的元素。
     *
     * @param nums 待搜索的整数数组。
     * @param left 搜索范围的左边界。
     * @param right 搜索范围的右边界。
     * @param k 要查找的第k小的元素，k从1开始。
     * @return 返回数组中第k小的元素。
     */
    public static int quickSelect(int[] nums, int left, int right, int k) {
        // 当左边界等于右边界时，返回该元素，表示已找到第k小的数
        if (left == right) {
            return nums[left];
        }
        int i = left - 1, j = right + 1, x = nums[left]; // 初始化分区点及其左右指针
        while (i < j) { // 当i小于j时，继续调整分区
            do {
                i++;
            } while (nums[i] < x); // 找到第一个大于等于x的元素
            do {
                j--;
            } while (nums[j] > x); // 找到第一个小于等于x的元素
            if (i < j) {
                swap(nums, i, j); // 交换两个元素，使分区更有序
            }
        }
        // 根据j与k的关系，递归地在左侧或右侧继续寻找
        if (k <= j) {
            return quickSelect(nums, left, j, k);
        } else {
            return quickSelect(nums, j + 1, right, k);
        }

    }


    /**
     * 快速排序算法 对给定的整型数组按照升序进行排序
     *
     * @param arr 待排序的整型数组
     * @param low 排序范围的起始索引
     * @param high 排序范围的结束索引
     */
    public static void quickSort(int[] arr, int low, int high) {
        // 当起始索引小于结束索引时继续排序
        if (low < high) {
            // 获取当前范围内的中间元素索引
            int mid = getMid(arr, low, high);
            // 对中间元素左侧的部分进行递归排序
            quickSort(arr, low, mid - 1);
            // 对中间元素右侧的部分进行递归排序
            quickSort(arr, mid + 1, high);
        }
    }

    /* 快速排序（尾递归优化，缩减堆栈深度） */
    //参考文档：https://cloud.tencent.com/developer/article/2062488
    //优化1：序列长度达到一定大小时，使用插入排序，长度为5~20之间
    //优化2：尾递归优化，可以缩减堆栈的深度，由原来的O(n)缩减为O(logn)
    //优化3：聚集元素
    //优化4：多线程处理快排
    public static void quickSortNew(int[] nums, int left, int right) {
        // 子数组长度为 1 时终止
        while (left < right) {
            // 哨兵划分操作
            int pivot = getMid(nums, left, right);
            // 对两个子数组中较短的那个执行快速排序
            if (pivot - left < right - pivot) {
                quickSortNew(nums, left, pivot - 1); // 递归排序左子数组
                left = pivot + 1; // 剩余未排序区间为 [pivot + 1, right]
            } else {
                quickSortNew(nums, pivot + 1, right); // 递归排序右子数组
                right = pivot - 1; // 剩余未排序区间为 [left, pivot - 1]
            }
        }
    }

    /**
     * 获取经过排序后的数组arr的中间位置索引。 该方法采用荷兰国旗问题的解决思路，将数组分为小于、等于和大于目标值（arr[low]）的三个部分。 其中，low为数组起始索引，high为数组结束索引。
     *
     * @param arr 待处理的数组
     * @param low 数组的起始索引
     * @param high 数组的结束索引
     * @return 经过排序后的数组中，等于目标值的元素最后出现的位置索引
     */
    private static int getMid(int[] arr, int low, int high) {
        int i = low, j = high;
        // 使用双指针法将数组划分为三个部分
        while (i < j) {
            // 从右向左查找第一个小于arr[low]的元素
            while (i < j && arr[j] >= arr[low]) {
                j--;
            }
            // 从左向右查找第一个大于arr[low]的元素
            while (i < j && arr[i] <= arr[low]) {
                i++;
            }
            // 交换找到的两个元素，将小于目标值的元素放到数组左侧
            if (i < j) {
                swap(arr, i, j);
            }
        }
        // 将等于目标值的元素放到数组的最后
        swap(arr, i, low);
        // 返回等于目标值的元素最后出现的位置索引
        return i;
    }


    public static int[] bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
        return arr;
    }


    public int hIndex(int[] citations) {
        int n = citations.length;
        int[] counter = new int[n + 1];
        // 对引用次数进行计数，超过n的按照n来计数
        for (int x : citations) {
            counter[Math.min(x, n)]++;
        }
        int total = 0;
        //在数组中找到引用次数大于或等于i的论文数
        for (int i = n; i >= 0; i--) {
            total += counter[i];
            if (total >= i) {
                return i;
            }
        }
        return 0;
    }

    /**
     * 将IP地址转换为整型数字。
     *
     * @param ip 输入的IP地址，格式为“xxx.xxx.xxx.xxx”。
     * @return 返回转换后的整型数字。
     */
    public static int ip2Int(String ip) {
        // 将IP地址按点号分割成四部分
        String[] ipArray = ip.split("\\.");
        // 将每部分转换为整型，并按位进行左移和或操作，完成IP地址到整数的转换
        return Integer.parseInt(ipArray[0]) << 24 | Integer.parseInt(ipArray[1]) << 16
            | Integer.parseInt(ipArray[2]) << 8 | Integer.parseInt(ipArray[3]);
    }

    /**
     * 将整型数字转换为IP地址字符串格式。
     *
     * @param ip 整型数字表示的IP地址。
     * @return 返回字符串形式的IP地址，例如"192.168.0.1"。
     */
    public static String int2Ip(int ip) {
        // 分别提取ip地址的每一部分，并将其转换为字符串格式
        StringBuilder sb = new StringBuilder();
        sb.append((ip >> 24 & 0xff)).append(".").append((ip >> 16 & 0xff)).append(".").append((ip >> 8 & 0xff))
            .append(".").append((ip & 0xff));
        return sb.toString();
    }

    /**
     * 将两个已排序的整数数组合并到第一个数组中。 nums1 是第一个数组，m 是 nums1 中已有的元素个数； nums2 是第二个数组，n 是 nums2 中已有的元素个数。 合并后的数组应保持有序。
     * 注意：不返回任何值，而是直接修改 nums1 数组。
     *
     * @param nums1 第一个有序整数数组，合并后的结果将存储在此数组中。
     * @param m nums1 中已有的元素个数。
     * @param nums2 第二个有序整数数组。
     * @param n nums2 中已有的元素个数。
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = nums1.length - 1; // 初始化 nums1 的指针 i 到末尾元素位置
        while (n > 0) { // 当 nums2 中还有元素时循环
            if (m > 0 && nums1[m - 1] > nums2[n - 1]) {
                // 如果 nums1 还有元素，并且 nums1 的最后一个元素大于 nums2 的最后一个元素
                nums1[i--] = nums2[m - 1]; // 将 nums2 的最后一个元素放入 nums1 的合适位置
                m--; // nums1 指针向前移动
            } else {
                // 其他情况下，将 nums1 的最后一个元素放入 nums1 的合适位置
                nums1[i--] = nums1[n - 1];
                n--; // nums2 指针向前移动
            }
        }
    }

    /**
     * 反转链表。 该函数接收一个链表的头节点，并返回反转后的链表的头节点。
     *
     * @param head 链表的头节点。
     * @return 反转后的链表的头节点。
     */
    public ListNode reverseList(ListNode head) {
        ListNode cur = head, pre = null; // 初始化当前节点cur为头节点，前一个节点pre为null
        while (cur != null) { // 遍历链表直到当前节点为null
            ListNode tmp = cur.next; // 临时变量tmp保存当前节点的下一个节点
            cur.next = pre; // 将当前节点的next指向前一个节点，实现反转
            pre = cur; // 更新前一个节点为当前节点
            cur = tmp; // 更新当前节点为下一个节点
        }
        return pre; // 返回反转后的链表的头节点
    }


    /**
     * 使用快慢指针法检测链表中是否存在环，并返回环的入口节点。 快指针每次移动两步，慢指针每次移动一步。如果链表中存在环，那么快慢指针最终会相遇。 相遇后，快指针重新从头开始移动，与慢指针以相同的速度移动，直到它们再次相遇，
     * 这个相遇点即为环的入口节点。
     *
     * @param head 链表的头节点
     * @return 返回环的入口节点。如果链表中无环，则返回null。
     */
    public ListNode detectCycle(ListNode head) {
        ListNode fast = head, slow = head;
        // 使用快慢指针寻找环的入口
        while (true) {
            if (fast == null || fast.next == null) {
                return null; // 如果快指针或快指针的下一个节点为null，则说明链表中无环
            }
            fast = fast.next.next; // 快指针每次移动两步
            slow = slow.next; // 慢指针每次移动一步
            if (fast == slow) {
                break; // 如果快慢指针相遇，则找到环的入口
            }
        }

        // 从头节点开始，与慢指针同时移动，直到再次相遇，相遇点即为环的入口
        fast = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }

        return fast; // 返回环的入口节点
    }

    /**
     * 检查给定的链表是否包含环。
     *
     * @param head 链表的头节点，类型为ListNode，表示链表的起始点。
     * @return 返回一个布尔值，如果链表中存在环，则返回true；否则返回false。
     */
    public boolean cycle(ListNode head) {
        // 链表为空或只有一个节点时不可能存在环
        if (head == null || head.next == null) {
            return false;
        }
        ListNode fast = head, slow = head;
        // 使用快慢指针法检测环
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            // 如果快慢指针相遇，则说明链表有环
            if (slow == fast) {
                return true;
            }
        }
        // 如果遍历结束快指针没有追上慢指针，说明链表无环
        return false;
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int[] randomIntArray(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(max);
        }
        return arr;
    }

    private static int[] randomIntArray(int len, int max, int sameLimit) {
        int[] arr = new int[len + sameLimit];
        for (int i = 0; i < len; i++) {
            arr[i] = ThreadLocalRandom.current().nextInt(max);
        }
        int sameInt = ThreadLocalRandom.current().nextInt(max);
        for (int i = 0; i < sameLimit; i++) {
            arr[len + i] = sameInt;
        }
        return arr;
    }

    private static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            printArray(arr[i]);
        }
    }

    //打印数组元素
    private static void printArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static class TreeNode {

        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public static class ListNode {

        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

}
