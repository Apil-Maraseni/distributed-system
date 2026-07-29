class BullyAlgorithm:
    def __init__(self, node_id, all_nodes):
        self.node_id = node_id
        self.all_nodes = all_nodes
        self.coordinator = max(all_nodes)
        self.is_active = True

    def send_election_message(self, node_id):
        print(f"Node {self.node_id}: Sending Election message to node {node_id}")

    def send_coordinator_message(self, node_id):
        print(f"Node {self.node_id}: Sending Coordinator message to node {node_id}")

    def start_election(self):
        if self.is_active:
            print(f"Node {self.node_id}: Starting Election...")
            for node in self.all_nodes:
                if node > self.node_id:
                    self.send_election_message(node)

    def handle_election_message(self, sender_id):
        if self.is_active and self.node_id > sender_id:
            self.send_coordinator_message(sender_id)
        elif self.is_active and self.node_id < sender_id:
            self.start_election()

    def handle_coordinator_message(self, sender_id):
        if self.is_active and self.node_id != self.coordinator:
            print(f"Node {self.node_id}: I'm not the Coordinator. Resigning...")
            self.start_election()
        else:
            print(f"Node {self.node_id}: I'm the Coordinator!")

    def node_failed(self):
        print(f"Node {self.node_id}: I've failed.")
        self.is_active = False


if __name__ == "__main__":
    all_nodes = [1, 2, 3, 4, 5]
    node_id = 3

    node = BullyAlgorithm(node_id, all_nodes)

    # Example scenarios:
    # Uncomment and run these to simulate different situations

    # Node starts an election:
    node.start_election()

    # Node receives an election message from a higher priority node:
    node.handle_election_message(4)

    # Node receives a coordinator message from the current coordinator:
    node.handle_coordinator_message(5)

    # Node fails:
    # node.node_failed()