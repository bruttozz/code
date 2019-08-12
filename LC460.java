class LFUCache {

    class Node {
        int key, val, freq;
        Node prev, next;

        public Node(int key, int val, Node prev, Node next) {
            this.key = key;
            this.val = val;
            this.prev = prev;
            this.next = next;
            freq = 1;
        }
    }

    Map<Integer, Node> frequency;
    Map<Integer, Node> nodes;
    Node head, tail;
    int cap;

    public LFUCache(int capacity) {
        frequency = new HashMap<>();
        nodes = new HashMap<>();
        head = new Node(0, 0, null, null);
        tail = new Node(0, 0, head, null);
        head.next = tail;
        cap = capacity;
    }

    private void pushNode(int key, int value) {
        Node prev = frequency.containsKey(1) ? frequency.get(1) : tail;
        Node newNode = new Node(key, value, prev.prev, prev);
        newNode.prev.next = newNode;
        newNode.next.prev = newNode;
        frequency.put(1, newNode);
        nodes.put(key, newNode);
    }

    private void popNode() {
        Node remove = tail.prev;
        remove.prev.next = tail;
        tail.prev = remove.prev;
        if (frequency.get(remove.freq) == remove) {
            frequency.remove(remove.freq);
        }
        nodes.remove(remove.key);
    }

    private void updateAndMoveNode(int key) {
        Node node = nodes.get(key);
        node.prev.next = node.next;
        node.next.prev = node.prev;
        if (frequency.containsKey(node.freq + 1)) {
            if (frequency.get(node.freq) == node) {
                if (node.next == tail || frequency.get(node.prev).freq != node.freq) {
                    frequency.remove(node.freq);
                } else {
                    frequency.put(node.freq, node.next);
                }
            }
            node.freq++;
            Node heads = frequency.get(node.freq);
            heads.prev.next = node;
            node.prev = heads.prev;
            node.next = heads;
            heads.prev = node;
        } else {
            if (frequency.get(node.freq) == node) {
                if (node.next == tail || frequency.get(node.prev).freq != node.freq) {
                    frequency.remove(node.freq);
                } else {
                    frequency.put(node.freq, node.next);
                }
                node.freq++;
                frequency.put(node.freq, node);
                node.prev.next = node;
                node.next.prev = node;
            } else {
                node.next = frequency.get(node.freq);
                node.prev = frequency.get(node.freq).prev;
                frequency.get(node.freq).prev.next = node;
                frequency.get(node.freq).prev = node;
                node.freq++;
                frequency.put(node.freq, node);
            }
        }
    }

    public int get(int key) {
        if (!nodes.containsKey(key)) {
            return -1;
        }
        updateAndMoveNode(key);
        return nodes.get(key).val;
    }

    public void put(int key, int value) {
        if (cap == 0) {
            return;
        }
        if (!nodes.containsKey(key)) {
            if (nodes.size() == cap) {
                popNode();
            }
            pushNode(key, value);
        } else {
            nodes.get(key).val = value;
            updateAndMoveNode(key);
        }
    }
}
