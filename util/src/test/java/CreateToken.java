import net.realmproject.util.model.Tokens;


public class CreateToken {
	public static void main(String[] args) {
		String token = Tokens.create();
		System.out.println(token);
	}
}
