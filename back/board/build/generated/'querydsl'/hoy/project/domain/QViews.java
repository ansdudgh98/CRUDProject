package hoy.project.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QViews is a Querydsl query type for Views
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QViews extends EntityPathBase<Views> {

    private static final long serialVersionUID = 1358989685L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QViews views = new QViews("views");

    public final QAccount account;

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QView view;

    public QViews(String variable) {
        this(Views.class, forVariable(variable), INITS);
    }

    public QViews(Path<? extends Views> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QViews(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QViews(PathMetadata metadata, PathInits inits) {
        this(Views.class, metadata, inits);
    }

    public QViews(Class<? extends Views> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new QAccount(forProperty("account")) : null;
        this.view = inits.isInitialized("view") ? new QView(forProperty("view"), inits.get("view")) : null;
    }

}

