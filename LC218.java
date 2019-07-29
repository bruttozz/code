class Solution {

    MaxHeap heap;

    public List<List<Integer>> getSkyline(int[][] buildings) {
        int n = buildings.length;
        List<int[]> events = new ArrayList<int[]>();
        for(int i = 0; i < n; i++) {
            events.add(new int[]{buildings[i][0], buildings[i][2], i});
            events.add(new int[]{buildings[i][1], -buildings[i][2], i});
        }
        events.sort((e1, e2) -> {
            return e1[0] == e2[0] ? e2[1] - e1[1] : e1[0] - e2[0];
        });
        List<List<Integer>> ans = new ArrayList<>();
        heap = new MaxHeap(n); //forgot
        for(int[] event : events) {
            int x = event[0];
            int h = event[1];
            int id = event[2];
            boolean entering = h > 0;
            h = Math.abs(h); //forgot
            List<Integer> a = new ArrayList<>();
            if (entering) {
                if (h > heap.max()) {
                    a.add(x);
                    a.add(h);
                    ans.add(a);
                }
                heap.add(h, id);
            } else {
                heap.remove(id);
                if (h > heap.max()) {
                    a.add(x);
                    a.add(heap.max());
                    ans.add(a);
                }
            }
        }
        return ans;
    }

//     private class MaxHeap {
//         int[] index;
//         List<int[]> nodes;

//         public MaxHeap(int size) {
//             index = new int[size];
//             nodes = new ArrayList<int[]>();
//         }

//         public void add(int key, int id) {
//             index[id] = nodes.size();
//             nodes.add(new int[]{key, id});
//             heapifyUp(index[id]);
//         }

//         public void remove(int id) {
//             int idx_to_remove = index[id]; //!!!
//             swapNodes(idx_to_remove, nodes.size() - 1);
//             index[id] = -1; //!!!
//             nodes.remove(nodes.size() - 1);
//             heapifyUp(idx_to_remove);
//             heapifyDown(idx_to_remove);
//         }

//         public int max() {
//             return empty() ? 0 : nodes.get(0)[0];
//         }

//         public boolean empty() {
//             return nodes.isEmpty();
//         }

//         public void heapifyUp(int idx) {
//             while(idx != 0) {
//                 int p = (idx - 1) / 2;
//                 if (nodes.get(idx)[0] <= nodes.get(p)[0]) {
//                     return;
//                 }
//                 swapNodes(idx, p);
//                 idx = p;
//             }
//         }

//         public void heapifyDown(int idx) {
//             while(true) {
//                 int c1 = 2 * idx + 1;
//                 int c2 = 2 * idx + 2;
//                 if (c1 >= nodes.size()) {
//                     return;
//                 }
//                 int c = (c2 < nodes.size() && nodes.get(c1)[0] < nodes.get(c2)[0]) ? c2 : c1;
//                 if (nodes.get(c)[0] <= nodes.get(idx)[0]) {
//                     return;
//                 }
//                 swapNodes(idx, c);
//                 idx = c;
//             }
//         }

// //         public void swapNodes(int id1, int id2) {
// //             int[] tmpNode = nodes.get(id1);
// //             nodes.set(id1, nodes.get(id2));
// //             nodes.set(id2, tmpNode);

// //             int tmpId = id1;
// //             id1 = id2;
// //             id2 = tmpId;
// //         }
//         private void swapNodes(int i, int j) {
//             int tmpIdx = index[nodes.get(i)[1]];
//             index[nodes.get(i)[1]] = index[nodes.get(j)[1]];
//             index[nodes.get(j)[1]] = tmpIdx;

//             int[] tmpNode = nodes.get(i);
//             nodes.set(i, nodes.get(j));
//             nodes.set(j, tmpNode);
//         }
//     }
    private class MaxHeap {
        // (key, id)
        private List<int[]> nodes;

        // idx[i] = index of building[i] in nodes
        private int[] idx;

        public MaxHeap(int size) {
            idx = new int[size];
            nodes = new ArrayList<int[]>();
        }

        public void add(int key, int id) {
            idx[id] = nodes.size();
            nodes.add(new int[]{key, id});
            heapifyUp(idx[id]);
        }

        public void remove(int id) {
            int idx_to_evict = idx[id];
            swapNode(idx_to_evict, nodes.size() - 1);
            idx[id] = -1;
            nodes.remove(nodes.size() - 1);
            heapifyUp(idx_to_evict);
            heapifyDown(idx_to_evict);
        }

        public Boolean empty() {
            return nodes.isEmpty();
        }

        public int max() {
            return empty() ? 0 : nodes.get(0)[0];
        }

        private void heapifyUp(int i) {
            while (i != 0) {
                int p = (i - 1) / 2;
                if (nodes.get(i)[0] <= nodes.get(p)[0])
                    return;

                swapNode(i, p);
                i = p;
            }
        }

        private void swapNode(int i, int j) {
            int tmpIdx = idx[nodes.get(i)[1]];
            idx[nodes.get(i)[1]] = idx[nodes.get(j)[1]];
            idx[nodes.get(j)[1]] = tmpIdx;

            int[] tmpNode = nodes.get(i);
            nodes.set(i, nodes.get(j));
            nodes.set(j, tmpNode);
        }

        private void heapifyDown(int i) {
            while (true) {
                int c1 = i*2 + 1;
                int c2 = i*2 + 2;

                if (c1 >= nodes.size()) return;

                int c = (c2 < nodes.size()
                      && nodes.get(c2)[0] > nodes.get(c1)[0]) ? c2 : c1;

                if (nodes.get(c)[0] <= nodes.get(i)[0])
                    return;

                swapNode(c, i);
                i = c;
            }
        }

    }
}
