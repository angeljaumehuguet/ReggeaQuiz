package com.example.reggeaquiz.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Question.class}, version = 1, exportSchema = false)
public abstract class QuestionDatabase extends RoomDatabase {
    
    private static final String DATABASE_NAME = "reggaequiz-db";
    
    public abstract QuestionDao questionDao();
    
    private static volatile QuestionDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    private static final ExecutorService databaseWriteExecutor = 
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    public static QuestionDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QuestionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            QuestionDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            databaseWriteExecutor.execute(() -> {
                QuestionDao dao = INSTANCE.questionDao();
                dao.deleteAllQuestions();
                
                // Agregar preguntas iniciales
                List<Question> questions = generateInitialQuestions();
                dao.insertQuestions(questions);
            });
        }
    };
    
    private static List<Question> generateInitialQuestions() {
        List<Question> questions = new ArrayList<>();
        
        // Añadimos al menos 10 preguntas sobre reggaeton (verdadero/falso)
        questions.add(new Question(
                "El reggaeton surgió originalmente en Panamá en los años 90.",
                true,
                "El reggaeton tiene sus orígenes en Panamá en los años 90, con influencias del reggae en español y el hip-hop latino, antes de popularizarse en Puerto Rico."));
        
        questions.add(new Question(
                "Daddy Yankee anunció su retiro de la música en 2022.",
                true,
                "Daddy Yankee anunció su retiro en marzo de 2022 con su último álbum 'Legendaddy' y su gira de despedida 'La Última Vuelta World Tour'."));
        
        questions.add(new Question(
                "'Gasolina' de Daddy Yankee fue lanzada en 2010.",
                false,
                "'Gasolina' fue lanzada en 2004 como parte del álbum 'Barrio Fino' y fue uno de los primeros éxitos internacionales del reggaeton."));
        
        questions.add(new Question(
                "Bad Bunny fue el artista más escuchado en Spotify a nivel mundial en 2020 y 2021.",
                true,
                "Bad Bunny fue el artista más escuchado en Spotify a nivel mundial tanto en 2020 como en 2021, consolidando el dominio global del reggaeton."));
        
        questions.add(new Question(
                "J Balvin es de nacionalidad colombiana.",
                true,
                "J Balvin (José Álvaro Osorio Balvin) nació en Medellín, Colombia, el 7 de mayo de 1985."));
        
        questions.add(new Question(
                "'Despacito' de Luis Fonsi y Daddy Yankee fue el primer video en YouTube en alcanzar 7 mil millones de visitas.",
                true,
                "'Despacito' hizo historia como el primer video en YouTube en superar los 7 mil millones de visitas, un hito para la música latina."));
        
        questions.add(new Question(
                "Ozuna tiene el récord Guinness por ser el artista con más videos que superan los mil millones de visitas en YouTube.",
                true,
                "En 2019, Ozuna recibió un récord Guinness por ser el artista con más videos que superaron los mil millones de visitas en YouTube."));
        
        questions.add(new Question(
                "El término 'reggaeton' es una mezcla de las palabras 'reggae' y 'marathon'.",
                false,
                "El término 'reggaeton' es una combinación de 'reggae' y 'maratón' (en español), aunque algunos también lo atribuyen a la combinación de 'reggae' y '-ton' (sufijo aumentativo en español)."));
        
        questions.add(new Question(
                "Nicky Jam y Daddy Yankee formaron un dúo llamado 'Los Cangris' en los inicios de sus carreras.",
                true,
                "Nicky Jam y Daddy Yankee formaron el dúo 'Los Cangris' a finales de los 90 y principios de los 2000, antes de separarse por problemas personales y reunirse años después."));
        
        questions.add(new Question(
                "El álbum 'Un Verano Sin Ti' de Bad Bunny fue lanzado en 2021.",
                false,
                "'Un Verano Sin Ti' fue lanzado el 6 de mayo de 2022 y se convirtió en uno de los álbumes más exitosos de la historia reciente del reggaeton."));
        
        questions.add(new Question(
                "Karol G es la artista femenina de reggaeton con más Grammy Latinos.",
                true,
                "Karol G ha ganado múltiples Grammy Latinos, convirtiéndose en la artista femenina de reggaeton más premiada en esta ceremonia."));
        
        questions.add(new Question(
                "El 'dembow' es un ritmo característico del reggaeton originario de Jamaica.",
                true,
                "El 'dembow' es un ritmo jamaiquino que fue adaptado en Panamá y luego en Puerto Rico, convirtiéndose en la base rítmica del reggaeton."));
        
        return questions;
    }
}
