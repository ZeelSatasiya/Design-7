class LFUCache {
    class Node{
        int key;
        int value;
        int frequency;
        Node next;
        Node prev;
        public Node(int key, int value){
            this.key = key;
            this.value = value;
            this.frequency = 1;
            this.next = null;
            this.prev = null;
        }
    }
    class DLList{
        int size;
        Node head, tail;
        public DLList(){
            size = 0;
            this.head = new Node(-1, -1);
            this.tail = new Node(-1, -1);
            head.next = tail;
            tail.prev = head;
        }
        public void addToHead(Node node){
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
            size++;
        }
        public void removeNode(Node node){
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
        }
        public Node removeFromTail(){
            if(size != 0){
                Node lastNode = tail.prev;
                removeNode(tail.prev);
                return lastNode;
            }
            return null;
        }
    }

    HashMap<Integer, Node> keyNodeMap;
    HashMap<Integer, DLList> freqMap;
    int capacity;
    int min;

    public LFUCache(int capacity) {
        this.min = 0;
        this.capacity = capacity;
        keyNodeMap = new HashMap<>();
        freqMap = new HashMap<>();
    }
    
    public int get(int key) {
        if(!keyNodeMap.containsKey(key)) return -1;
        Node curr = keyNodeMap.get(key);
        update(curr);
        return curr.value;
    }
    
    public void put(int key, int value) {
        if(keyNodeMap.containsKey(key)){
            Node node = keyNodeMap.get(key);
            node.value = value;
            update(node);
            return;
        }
        if(keyNodeMap.size() == capacity){
            DLList freqList = freqMap.get(min);
            Node removed = freqList.removeFromTail();
            keyNodeMap.remove(removed.key);
        }
        Node curr = new Node(key, value);
        min = 1;
        keyNodeMap.put(key, curr);
        if(!freqMap.containsKey(1)){
            freqMap.put(1, new DLList());
        }
        DLList newfreqList = freqMap.get(1);
        newfreqList.addToHead(curr);
    }

    public void update(Node curr){
        DLList freqList = freqMap.get(curr.frequency);
        freqList.removeNode(curr);
        if(freqList.size == 0 && min == curr.frequency){
            min++;
        }
        curr.frequency++;
        // System.out.println("freq: " + curr.frequency);
        if(!freqMap.containsKey(curr.frequency)){
            // System.out.println(curr.key);
            freqMap.put(curr.frequency, new DLList());
        }
        DLList newfreqList = freqMap.get(curr.frequency);
        newfreqList.addToHead(curr); 
    }
}

/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
