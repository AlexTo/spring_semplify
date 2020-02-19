import React, {useState} from 'react';
import {Link as RouterLink} from 'react-router-dom';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import moment from 'moment';
import {makeStyles} from '@material-ui/styles';
import {
  Avatar,
  Button,
  Card,
  CardContent,
  CardHeader,
  Divider,
  Grid,
  IconButton,
  Link,
  Tooltip,
  Typography,
  colors
} from '@material-ui/core';
import ShareIcon from '@material-ui/icons/Share';
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';

const useStyles = makeStyles((theme) => ({
  root: {},
  header: {
    paddingBottom: 0
  },
  content: {
    padding: 0,
    '&:last-child': {
      paddingBottom: 0
    }
  },
  description: {
    padding: theme.spacing(2, 3, 1, 3)
  },
  learnMoreButton: {
    marginLeft: theme.spacing(2)
  },
  likedButton: {
    color: colors.red[600]
  },
  shareButton: {
    marginLeft: theme.spacing(1)
  },
  details: {
    padding: theme.spacing(2, 3)
  }
}));

function SearchHitCard({searchHit, className, ...rest}) {
  const classes = useStyles();
  const [liked, setLiked] = useState(searchHit.liked);
  const {content} = searchHit;

  const handleLike = () => {
    setLiked(true);
  };

  const handleUnlike = () => {
    setLiked(false);
  };

  return (
    <Card
      {...rest}
      className={clsx(classes.root, className)}>
      <CardHeader
        className={classes.header}
        disableTypography
        title={(
          <Link
            color="textPrimary"
            component={RouterLink}
            to={content.uri}
            variant="h5">
            {content.label}
          </Link>
        )}
      />
      <CardContent className={classes.content}>
        <div className={classes.description}>
          <Typography
            color="textSecondary"
            variant="subtitle2">
            {searchHit.highlightFields[0].highlights[0].replace(/<em>/g, "").replace(/<\/em>/g, "")}
          </Typography>
        </div>
        <Divider/>
        <div className={classes.details}>
          <Grid
            alignItems="center"
            container
            justify="space-between"
            spacing={3}>
            <Grid item>
              {liked ? (
                <Tooltip title="Unlike">
                  <IconButton
                    className={classes.likedButton}
                    onClick={handleUnlike}
                    size="small">
                    <FavoriteIcon/>
                  </IconButton>
                </Tooltip>
              ) : (
                <Tooltip title="Like">
                  <IconButton
                    className={classes.likeButton}
                    onClick={handleLike}
                    size="small">
                    <FavoriteBorderIcon/>
                  </IconButton>
                </Tooltip>
              )}
              <Tooltip title="Share">
                <IconButton
                  className={classes.shareButton}
                  size="small">
                  <ShareIcon/>
                </IconButton>
              </Tooltip>
            </Grid>
          </Grid>
        </div>
      </CardContent>
    </Card>
  );
}

SearchHitCard.propTypes = {
  className: PropTypes.string,
  searchHit: PropTypes.object.isRequired
};

export default SearchHitCard;
