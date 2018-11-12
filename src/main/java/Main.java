import org.hibernate.Query;
import org.hibernate.Session;

import persistent.HibernateUtil;

import java.util.List;

public class Main {

    public static void main( String[] args )
    {
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();
        Query query = session.createQuery("from Message where sender = 'press.release@enron.com' ");
        List list = query.list();

        Message message = (Message)list.get(0);
        System.out.println(message.getDate());
        System.out.println(list.size());

        Query query2 = session.createQuery("from Employee where firstName = 'Lynn' ");
        List list2 = query2.list();

        Employee employee = (Employee) list2.get(0);
        System.out.println(employee.getLastName());
        session.getTransaction().commit();
        session.getSessionFactory().close();
    }
}
