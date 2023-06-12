package dev.dotspace.network.node.profile.db;


import dev.dotspace.network.library.profile.IProfile;
import dev.dotspace.network.library.profile.ProfileType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Entity
@Table(name = "Profile",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"Uid", "Platform"})})
@NoArgsConstructor
@Accessors(fluent = true)
public final class ProfileEntity implements IProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    /**
     * See {@link IProfile#uniqueId()}.
     */
    @Column(name = "Uid", nullable = false, unique = true)
    @Getter
    private String uniqueId;

    /**
     * See {@link IProfile#profileType()}.
     */
    @Column(name = "Platform", nullable = false)
    private Integer profileType;

    public ProfileEntity(@Nullable final String uniqueId,
                         final int profileType) {
        this.uniqueId = Objects.requireNonNull(uniqueId);
        this.profileType = profileType;
    }

    /**
     * See {@link IProfile#profileType()}.
     */
    @Override
    public @NotNull ProfileType profileType() {
        return ProfileType.fromId(this.profileType);
    }
}