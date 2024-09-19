import Model.ScriptModel;
import View.ScriptView;
import Controller.ScriptController;

public class Main {
    public static void main(String[] args) {

        ScriptModel model = new ScriptModel();
        ScriptView view = new ScriptView();
        ScriptController controller = new ScriptController(model, view);
    }
}
