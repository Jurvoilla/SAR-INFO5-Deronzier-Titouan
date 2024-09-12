package task1;

class CircularBuffer {
	 byte[] ring;
	 int tail,head;
	 CircularBuffer(byte[] r) {
	 ring = r;
	 }
	 // Returns true if this buffer is full, false otherwise
	 boolean full() {
		return false;
	}
	 // Returns true if this buffer is empty, false otherwise
	 boolean empty() {
		return false;
	}
	 // Pushes the give element in the buffer
	 // Throws illegal-state exception if full
	 void push(byte elem) {
	}
	 // returns the next available element
	 // Throws illegal-state exception if empty
	 byte pull() {
		return 0;
	}
	}

