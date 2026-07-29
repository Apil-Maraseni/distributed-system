class LamportClock:
    def __init__(self):
        self.time = 0

    def increment(self):
        self.time += 1

    def update(self, received_time):
        self.time = max(self.time, received_time) + 1

    def get_time(self):
        return self.time

    def send_message(self):
        self.increment()
        return self.time


if __name__ == "__main__":
    # Create three Lamport clock instances for three processes
    process1_clock = LamportClock()
    process2_clock = LamportClock()
    process3_clock = LamportClock()

    # Process 1 does some work
    process1_clock.increment()
    print(f"Process 1 time: {process1_clock.get_time()}")

    # Process 2 does some work
    process2_clock.increment()
    print(f"Process 2 time: {process2_clock.get_time()}")

    # Process 1 sends a message to Process 2
    sent_time = process1_clock.send_message()
    print(f"Process 1 sends message at time: {sent_time}")
    process2_clock.update(sent_time)
    print(f"Process 2 updated time: {process2_clock.get_time()}")

    # Process 2 sends a message to Process 3
    sent_time = process2_clock.send_message()
    print(f"Process 2 sends message at time: {sent_time}")
    process3_clock.update(sent_time)
    print(f"Process 3 updated time: {process3_clock.get_time()}")

    # Process 3 does some work
    process3_clock.increment()
    print(f"Process 3 time: {process3_clock.get_time()}")

    # Final clock values after communication
    print(f"Process 1 final time: {process1_clock.get_time()}")
    print(f"Process 2 final time: {process2_clock.get_time()}")
    print(f"Process 3 final time: {process3_clock.get_time()}")