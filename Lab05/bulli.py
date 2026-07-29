class BullyAlgorithm:
    def __init__(self, num_processes):
        self.num_processes = num_processes
        self.processes = [i for i in range(1, num_processes + 1)]
        self.coordinator = num_processes  # Process 5 is initially coordinator

    def start_election(self, process_id):
        print(f"Process {process_id} initiates election.")
        if process_id == self.num_processes:
            print(f"Process {process_id} is already the coordinator.")
            return
        for higher_process in range(process_id + 1, self.num_processes + 1):
            print(f"Process {process_id} sends election message to process {higher_process}.")
        self.coordinator = None  # Reset coordinator until one is elected
        self.elect_coordinator()

    def elect_coordinator(self):
        if self.coordinator is None:
            self.coordinator = max(self.processes)
            print(f"Process {self.coordinator} becomes the coordinator.")


if __name__ == "__main__":
    num_processes = 5
    bully = BullyAlgorithm(num_processes)

    while True:
        start_process = int(input("Enter the process to start election (1 to 5): "))
        if start_process not in bully.processes:
            print("Invalid process number. Please enter a number between 1 and 5.")
            continue

        bully.start_election(start_process)