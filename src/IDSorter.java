import java.util.Comparator;
 
public class IDSorter implements Comparator<Mensagem> 
{
    @Override
    public int compare(Mensagem m1, Mensagem m2) {
        return Integer.valueOf(m1.getId()).compareTo(Integer.valueOf(m2.getId()));
    }
}