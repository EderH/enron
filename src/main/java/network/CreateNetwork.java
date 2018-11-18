package network;

import POJO.Employee;
import Entities.Mail;

import java.util.List;


public class CreateNetwork {
    private List mails;
    private List employees;
    
    public CreateNetwork(List mails, List employees) {
        this.mails = mails;
        this.employees = employees;
    }
    
    public Network call() {
        Network network = new Network();

        for (int i = 0; i < employees.size(); i++) {
            Employee employee = (Employee)employees.get(i);
            network.addNode(new Node(employee.getEmail_id()));
        }
        for (int i = 0; i < mails.size(); i++) {
            Mail mail = (Mail)mails.get(i);
            Node nodeSender = network.getNode(mail.getSender());
            if(nodeSender == null) {
                nodeSender = new Node(mail.getSender());
                network.addNode(nodeSender);
            }
            nodeSender.increaseOutbound();
            for (String receiver : mail.getTo()) {
                Node nodeReceiver = network.getNode(receiver);
                if(nodeReceiver == null) {
                    nodeReceiver = new Node(receiver);
                    network.addNode(nodeReceiver);
                }
                nodeReceiver.increaseInbound();
                Edge edge = new Edge(nodeSender,nodeReceiver);
                network.addEdge(edge);
            }


        }
        return network;
    }
}
