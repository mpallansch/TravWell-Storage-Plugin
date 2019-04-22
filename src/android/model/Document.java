package gov.cdc.oid.ncezid.travwell.model;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Created by parker on 12/6/13.
 */
public class Document extends Model {
    public static final String TABLE = "Document";

    public Document(Trip trip, String name, String category, String localImagePath, String remoteImagePath, Profile profile, String localThumbPath, String remoteThumbPath) {
        this.trip = trip;
        this.name = name;
        this.category = category;
        this.localImagePath = localImagePath;
        this.remoteImagePath = remoteImagePath;
        this.profile = profile;
        this.localThumbPath = localThumbPath;
        this.remoteThumbPath = remoteThumbPath;
    }

    public Document() {
        super();
    }

    @Column(name = Trip.TABLE)
    public Trip trip;

    @Column(name = Keys.NAME)
    public String name;

    @Column(name = Keys.CATEGORY)
    public String category;

    @Column(name = Keys.LOCAL_IMAGE_PATH)
    public String localImagePath;

    @Column(name = Keys.REMOTE_IMAGE_PATH)
    public String remoteImagePath;

    @Column(name = Profile.TABLE)
    public Profile profile;

    @Column(name = Keys.LOCAL_THUMB_PATH)
    public String localThumbPath;

    @Column(name = Keys.REMOTE_THUMB_PATH)
    public String remoteThumbPath;

    public static final class Keys {
        public static final String NAME = "Name";
        public static final String CATEGORY = "Category";
        public static final String LOCAL_IMAGE_PATH = "LocalImagePath";
        public static final String REMOTE_IMAGE_PATH = "RemoteImagePath";
        public static final String LOCAL_THUMB_PATH = "LocalThumbPath";
        public static final String REMOTE_THUMB_PATH = "RemoteThumbPath";
    }

    public Trip getTrip() {
        if (trip != null && trip.getName() != null) {
            trip = DatabaseQueries.getTrip(trip.getId());
        }
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * There should only be two categories
     * All Document
     * Current Trip
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public String getRemoteImagePath() {
        return remoteImagePath;
    }

    public void setRemoteImagePath(String remoteImagePath) {
        this.remoteImagePath = remoteImagePath;
    }

    /**
     * Deletes the imageFile then the database entry
     */
    public void deepDelete() {
        deleteFile(localImagePath);
        deleteFile(localThumbPath);
        delete();
    }

    /**
     * Deletes files with the file path and null checks and exists checks
     *
     * @param filePath
     */
    public void deleteFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file != null) {
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public Profile getProfile() {
        if (profile != null) {
            profile = new Select().from(Profile.class).where("Id =?", profile.getId()).executeSingle();
        }
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getLocalThumbPath() {
        return localThumbPath;
    }

    public void setLocalThumbPath(String localThumbPath) {
        this.localThumbPath = localThumbPath;
    }

    public String getRemoteThumbPath() {
        return remoteThumbPath;
    }

    public void setRemoteThumbPath(String remoteThumbPath) {
        this.remoteThumbPath = remoteThumbPath;
    }

    public static class DocumentDeepDeleteTask extends AsyncTask<Void, Void, Void> {
        public Document document;
        public HashSet<Document> documentsSet;
        public List<Document> documents;

        public DocumentDeepDeleteTask(Document document) {
            this.document = document;
        }

        public DocumentDeepDeleteTask(HashSet<Document> documents) {
            this.documentsSet = documents;
        }

        public DocumentDeepDeleteTask(List<Document> documents) {
            this.documents = documents;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (document != null) {
                document.deepDelete();
            } else if (documentsSet != null) {
                for (Document doc : documentsSet) {
                    doc.deepDelete();
                }
            } else if (documents != null) {
                for (Document doc : documents) {
                    doc.deepDelete();
                }
            }
            return null;
        }
    }
}