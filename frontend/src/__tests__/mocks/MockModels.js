export class ActorDto {
    constructor(actorId, actorName, profilePath, url, mediasUrl) {
        this.actorId = actorId;
        this.actorName = actorName;
        this.profilePath = profilePath;
        this.url = url;
        this.mediasUrl = mediasUrl;
    }
}

export class BanMessageDto {
    constructor({ banMessage, url, userUrl }) {
        this.banMessage = banMessage;
        this.url = url;
        this.userUrl = userUrl;
    }
}

export class CommentDto {
    constructor({
                    id,
                    reviewId,
                    mediaId,
                    content,
                    commentLikes,
                    commentDislikes,
                    username,
                    spamReportsUrl,
                    hateReportsUrl,
                    abuseReportsUrl,
                    privacyReportsUrl,
                    reportsUrl,
                    url,
                    userUrl,
                    reviewUrl,
                    feedbackUrl
                }) {
        this.id = id;
        this.reviewId = reviewId;
        this.mediaId = mediaId;
        this.content = content;
        this.commentLikes = commentLikes;
        this.commentDislikes = commentDislikes;
        this.username = username;
        this.spamReportsUrl = spamReportsUrl;
        this.hateReportsUrl = hateReportsUrl;
        this.abuseReportsUrl = abuseReportsUrl;
        this.privacyReportsUrl = privacyReportsUrl;
        this.reportsUrl = reportsUrl;
        this.url = url;
        this.userUrl = userUrl;
        this.reviewUrl = reviewUrl;
        this.feedbackUrl = feedbackUrl;
    }
}

export class CommentReportDto {
    constructor({
                    url,
                    reportId,
                    type,
                    reportDate,
                    reportedByUrl,
                    commentUrl
                }) {
        this.url = url;
        this.reportId = reportId;
        this.type = type;
        this.reportDate = reportDate; // en JS usamos Date o string ISO
        this.reportedByUrl = reportedByUrl;
        this.commentUrl = commentUrl;
    }
}

export class DirectorDto {
    constructor({
                    directorId,
                    name,
                    totalMedia,
                    url,
                    mediasUrl
                }) {
        this.directorId = directorId;
        this.name = name;
        this.totalMedia = totalMedia;
        this.url = url;
        this.mediasUrl = mediasUrl;
    }
}

export class GenreDto {
    constructor({
                    genreId,
                    genreName,
                    url,
                    mediasUrl
                }) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.url = url;
        this.mediasUrl = mediasUrl;
    }
}


export class MediaDto {
    constructor({
                    id,
                    type,
                    name,
                    originalLanguage,
                    adult,
                    releaseDate,
                    overview,
                    backdropPath,
                    posterPath,
                    trailerLink,
                    tmdbRating,
                    status,
                    totalRating,
                    voteCount,
                    providersUrl,
                    genresUrl,
                    reviewsUrl,
                    actorsUrl,
                    url
                }) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.originalLanguage = originalLanguage;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.trailerLink = trailerLink;
        this.tmdbRating = tmdbRating;
        this.status = status;
        this.totalRating = totalRating;
        this.voteCount = voteCount;
        this.providersUrl = providersUrl;
        this.genresUrl = genresUrl;
        this.reviewsUrl = reviewsUrl;
        this.actorsUrl = actorsUrl;
        this.url = url;
    }
}

export class MediaIdListIdDto {
    constructor({ mediaId, moovieListId, customOrder, baseUrl }) {
        this.mediaId = mediaId;
        this.mediaUrl = `${baseUrl}/medias/${mediaId}`;
        this.moovieListId = moovieListId;
        this.listUrl = `${baseUrl}/lists/${moovieListId}`;
        this.customOrder = customOrder;
    }
}

export class MoovieListDto {
    constructor({
                    id,
                    name,
                    createdBy,
                    description,
                    type,
                    likes,
                    followers,
                    mediaCount,
                    movieCount,
                    images,
                    url,
                    contentUrl,
                    creatorUrl,
                    reportsUrl,
                    spamReportsUrl,
                    hateReportsUrl,
                    abuseReportsUrl,
                    privacyReportsUrl,
                    recommendedListsUrl,
                    likesUrl,
                    followersUrl,
                    reviewsUrl
                }) {
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.description = description;
        this.type = type;
        this.likes = likes;
        this.followers = followers;
        this.mediaCount = mediaCount;
        this.movieCount = movieCount;
        this.images = images;
        this.url = url;
        this.contentUrl = contentUrl;
        this.creatorUrl = creatorUrl;
        this.reportsUrl = reportsUrl;
        this.spamReportsUrl = spamReportsUrl;
        this.hateReportsUrl = hateReportsUrl;
        this.abuseReportsUrl = abuseReportsUrl;
        this.privacyReportsUrl = privacyReportsUrl;
        this.recommendedListsUrl = recommendedListsUrl;
        this.likesUrl = likesUrl;
        this.followersUrl = followersUrl;
        this.reviewsUrl = reviewsUrl;
    }
}


export class MoovieListReportDto {
    constructor({
                    url,
                    reportId,
                    type,
                    report_date,
                    reportedByUrl,
                    moovieListUrl,
                }) {
        this.url = url;
        this.reportId = reportId;
        this.type = type;
        this.report_date = report_date; // Espera objeto Date o ISO string
        this.reportedByUrl = reportedByUrl;
        this.moovieListUrl = moovieListUrl;
    }
}
export class MoovieListReviewDto {
    constructor({
                    id,
                    moovieListid,
                    reviewLikes,
                    lastModified,
                    reviewContent,
                    reportsUrl,
                    spamReportsUrl,
                    hateReportsUrl,
                    abuseReportsUrl,
                    likesUrl,
                    privacyReportsUrl,
                    username,
                    url,
                    userUrl,
                    moovieListUrl,
                }) {
        this.id = id;
        this.moovieListid = moovieListid;
        this.reviewLikes = reviewLikes;
        this.lastModified = lastModified;
        this.reviewContent = reviewContent;
        this.reportsUrl = reportsUrl;
        this.spamReportsUrl = spamReportsUrl;
        this.hateReportsUrl = hateReportsUrl;
        this.abuseReportsUrl = abuseReportsUrl;
        this.likesUrl = likesUrl;
        this.privacyReportsUrl = privacyReportsUrl;
        this.username = username;
        this.url = url;
        this.userUrl = userUrl;
        this.moovieListUrl = moovieListUrl;
    }
}


export class MoovieListReviewReportDto {
    constructor({
                    url,
                    reportId,
                    type,
                    report_date,
                    reportedByUrl,
                    moovieListReviewUrl,
                }) {
        this.url = url;
        this.reportId = reportId;
        this.type = type;
        this.report_date = report_date; // Date o ISO string
        this.reportedByUrl = reportedByUrl;
        this.moovieListReviewUrl = moovieListReviewUrl;
    }
}

class MovieDto extends MediaDto {
    constructor(runtime, budget, revenue, directorUrl) {
        super();
        this.runtime = runtime;
        this.budget = budget;
        this.revenue = revenue;
        this.directorUrl = directorUrl;
    }
}

export class ProviderDto {
    constructor({
                    providerId,
                    providerName,
                    logoPath,
                    url,
                    mediasUrl
                }) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.logoPath = logoPath;
        this.url = url;
        this.mediasUrl = mediasUrl;
    }
}

export class ReviewDto {
    constructor({
                    id,
                    mediaId,
                    rating,
                    reviewContent,
                    likes,
                    username,
                    lastModified,
                    reportsUrl,
                    spamReportsUrl,
                    hateReportsUrl,
                    privacyReportsUrl,
                    abuseReportsUrl,
                    url,
                    userUrl,
                    mediaUrl,
                    commentsUrl,
                    likesUrl
                }) {
        this.id = id;
        this.mediaId = mediaId;
        this.rating = rating;
        this.reviewContent = reviewContent;
        this.likes = likes;
        this.username = username;
        this.lastModified = lastModified;
        this.reportsUrl = reportsUrl;
        this.spamReportsUrl = spamReportsUrl;
        this.hateReportsUrl = hateReportsUrl;
        this.privacyReportsUrl = privacyReportsUrl;
        this.abuseReportsUrl = abuseReportsUrl;
        this.url = url;
        this.userUrl = userUrl;
        this.mediaUrl = mediaUrl;
        this.commentsUrl = commentsUrl;
        this.likesUrl = likesUrl;
    }
}

export class ReviewReportDto {
    constructor({
                    url,
                    reportId,
                    type,
                    report_date,
                    reportedByUrl,
                    reviewUrl
                }) {
        this.url = url;
        this.reportId = reportId;
        this.type = type;
        this.report_date = report_date;
        this.reportedByUrl = reportedByUrl;
        this.reviewUrl = reviewUrl;
    }
}

export class TvCreatorsDto {
    constructor({
                    id,
                    creatorName,
                    url,
                    mediasUrl
                }) {
        this.id = id;
        this.creatorName = creatorName;
        this.url = url;
        this.mediasUrl = mediasUrl;
    }
}


export class TVSerieDto extends MediaDto {
    constructor({
                    lastAirDate,
                    nextEpisodeToAir,
                    numberOfEpisodes,
                    numberOfSeasons,
                    creatorsUrl,
                    ...mediaProps
                }) {
        super(mediaProps);
        this.lastAirDate = lastAirDate;
        this.nextEpisodeToAir = nextEpisodeToAir;
        this.numberOfEpisodes = numberOfEpisodes;
        this.numberOfSeasons = numberOfSeasons;
        this.creatorsUrl = creatorsUrl;
    }
}

class UserCommentFeedbackDto {
    constructor(commentId, username, url, feedback,commentUrl, userUrl) {
        this.commentId = commentId;
        this.username = username;
        this.url = url;
        this.feedback = feedback;

        this.commentUrl = commentUrl ;
        this.userUrl = userUrl;
    }
}

export class UserDto {
    constructor(username, role, milkyPoints, hasBadge, imageUrl, url, moovieListsUrl, defaultPrivateMoovieListsUrl, publicMoovieListsUrl, privateMoovieListsUrl, likedMoovieListsUrl, followedMoovieListsUrl, likedMoovieListsReviewsUrl, likedReviewsUrl, moovieListReviewsUrl, reviewsUrl, banMessageUrl, commentFeedbackUrl, commentsUrl) {
        this.username = username;
        this.role = role;
        this.milkyPoints = milkyPoints;
        this.hasBadge = hasBadge;
        this.imageUrl = imageUrl;

        this.url = url;
        this.moovieListsUrl = moovieListsUrl;
        this.defaultPrivateMoovieListsUrl = defaultPrivateMoovieListsUrl;
        this.publicMoovieListsUrl = publicMoovieListsUrl;
        this.privateMoovieListsUrl = privateMoovieListsUrl;
        this.likedMoovieListsUrl = likedMoovieListsUrl;
        this.followedMoovieListsUrl = followedMoovieListsUrl;
        this.likedMoovieListsReviewsUrl = likedMoovieListsReviewsUrl;
        this.likedReviewsUrl = likedReviewsUrl;
        this.moovieListReviewsUrl = moovieListReviewsUrl;
        this.reviewsUrl = reviewsUrl;
        this.banMessageUrl = banMessageUrl;
        this.commentFeedbackUrl = commentFeedbackUrl;
        this.commentsUrl = commentsUrl;
    }
}

class UserListIdDto {
    constructor(moovieListId, moovieListUrl, username, userUrl, url) {
        this.moovieListId = moovieListId;
        this.moovieListUrl = moovieListUrl;
        this.username = username;
        this.userUrl = userUrl;
        this.url = url;
    }
}

class UserMoovieListReviewDto {
    constructor(moovieListReviewId, username, moovieListReviewUrl, userUrl, url) {
        this.moovieListReviewId = moovieListReviewId;
        this.username = username;
        this.moovieListReviewUrl = moovieListReviewUrl;
        this.userUrl = userUrl;
        this.url = url;
    }
}

class UserReviewDto {
    constructor(reviewId, username, reviewUrl, userUrl, url) {
        this.reviewId = reviewId;
        this.username = username;
        this.reviewUrl = reviewUrl;
        this.userUrl = userUrl;
        this.url = url;
    }
}
