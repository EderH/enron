import Entities.Mail;
import POJO.Employee;
import POJO.Message;
import POJO.RecipientInfo;
import network.CreateNetwork;
import network.Edge;
import network.Network;
import network.Node;
import org.hibernate.Query;
import org.hibernate.Session;

import persistent.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main( String[] args )
    {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Query query = session.createQuery("FROM Message group by body, sender, subject having count(*) < 2 ");
        query.setMaxResults(5);
        List<Message> messageList = query.list();

        List<Mail> mails = new ArrayList<Mail>();

        for (Message message : messageList) {
            Mail mail = new Mail((message));
            List<String> to = new ArrayList<String>();
            Query query3 = session.createQuery("FROM RecipientInfo WHERE mid = :mid");
            query3.setParameter("mid", mail.getMid());
            List<RecipientInfo> receivers = query3.list();
            for (RecipientInfo receiver:receivers) {
                to.add(receiver.getRvalue());
            }
            mail.setTo(to);
            mails.add(mail);
        }

        /*KNearestNeighbor kNearestNeighbor = new KNearestNeighbor(list.size());
        long[][] distanceMatrix = kNearestNeighbor.call(list);
        printMatrix(distanceMatrix);*/

        Query query2 = session.createQuery("from Employee");
        List<Employee> employeeList = query2.list();

        session.getTransaction().commit();
        session.getSessionFactory().close();
        
        System.out.println("Network");
        CreateNetwork createNetwork = new CreateNetwork(mails,employeeList);
        Network network = createNetwork.call();
        System.out.println(network.getNodes().size());
        System.out.println(network.getEdges().size());
    }

    private static void printMatrix(long[][] matrix) {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                System.out.print("| " + matrix[x][y] + " |");
            }
            System.out.println();
        }
    }
}
