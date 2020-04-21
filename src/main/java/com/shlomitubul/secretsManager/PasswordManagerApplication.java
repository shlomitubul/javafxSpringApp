package com.shlomitubul.secretsManager;

import com.shlomitubul.secretsManager.view.FxmlView;
import com.shlomitubul.secretsManager.view.StageManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PasswordManagerApplication extends Application {

	protected ConfigurableApplicationContext springContext;
	protected StageManager stageManager;


	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() throws Exception {
		springContext = bootstrapSpringApplicationContext();
	}

	@Override
	public void start(Stage stage) throws Exception {
		stageManager = springContext.getBean(StageManager.class, stage);
		displayInitialScene();
	}

	@Override
	public void stop() throws Exception {
		springContext.close();
	}

	protected void displayInitialScene() {
		stageManager.switchScene(FxmlView.LOGIN);
	}


	private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(PasswordManagerApplication.class);
		String[] args = getParameters().getRaw().stream().toArray(String[]::new);
		builder.headless(false);  // useful when need headless testing
		return builder.run(args);
	}

}
